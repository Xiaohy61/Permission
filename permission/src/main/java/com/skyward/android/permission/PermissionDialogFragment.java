package com.skyward.android.permission;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.PermissionChecker;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 *
 * @author skyward
 *
 */
public class PermissionDialogFragment extends DialogFragment {

    private static final String PERMISSIONS = "permissions";


    private static final int PERMISSION_REQUEST_CODE = 64;
    private String[] permissions = new String[1];

    private RelativeLayout tipLayout;
    private TextView btnCancel;
    private TextView btnSure;
    private OnPermissionListener mListener;
    private  boolean require = true;
    String[] unGetPermissions;
    private Context mContext;

    public PermissionDialogFragment() {
        // Required empty public constructor
    }


    public static PermissionDialogFragment newInstance(String[] permissions) {
        PermissionDialogFragment fragment = new PermissionDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(PERMISSIONS, permissions);
        fragment.setArguments(args);

        return fragment;
    }

    /**'
     *
     * @param context 上下文
     */
    public void setActivityContext(Context context){
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.windowAnimations = R.style.appDialog;

        window.setGravity(Gravity.CENTER);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.8f);
        window.setAttributes(layoutParams);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            permissions = getArguments().getStringArray(PERMISSIONS);
        }

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_permission_dialog, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    /**
     * 权限监听回调
     * @param listener listener
     */
    public void onPermissionListener(OnPermissionListener listener){
        this.mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    protected void initView(View view) {

        tipLayout = view.findViewById(R.id.tip_layout);
        btnCancel = view.findViewById(R.id.btn_cancel);
        TextView tip = view.findViewById(R.id.tip_content2);
        btnSure = view.findViewById(R.id.btn_sure);
        tip.setText("请点击：\"设置\"-\"权限\"-打开所需权限。");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onCancelClick();
                }

                dismiss();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetting(mContext);
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();


        int unGetPermissionCount = 0;

        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                unGetPermissionCount++;
            }
        }

        unGetPermissions = new String[unGetPermissionCount];
        int index = 0;

        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                unGetPermissions[index] = permission;
                index++;
            }
        }


        //有些手机，比如小米，用户手动设置了允许权限，其实还没有真正获取到授权，要再次请求才可以获取到
        if (require) {
            tipLayout.setVisibility(View.GONE);
            if (RequestPermission.hasPermission(mContext, unGetPermissions)) {
                dismiss();
                if(mListener != null){
                    mListener.onPermissionSuccess();
                }
            } else {
                //请求申请权限,这个方法会触发onResume
                requestPermissions(unGetPermissions, PERMISSION_REQUEST_CODE);
                require = false;
            }
        } else {
            if (RequestPermission.hasPermission(mContext, unGetPermissions)) {
                dismiss();
                if(mListener != null){
                    mListener.onPermissionSuccess();
                }
            }
            if(mListener != null){
                mListener.onPermissionFailure(unGetPermissions);
            }
            require = true;
        }

    }



    /**
     * 去设置，用户手动设置授权
     * @param context 上下文
     */
    private  void gotoSetting(@NonNull Context context) {
        tipLayout.setVisibility(View.GONE);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE && isHasPermission(grantResults) && RequestPermission.hasPermission(mContext,permissions)) {
            tipLayout.setVisibility(View.GONE);
            if(mListener != null){
                mListener.onPermissionSuccess();
            }
        } else {
            //如果授权失败，把弹窗显示出来让用户手动设置授权
            tipLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断一组授权结果是否为授权通过
     * @param grantResult  grantResult
     * @return boolean
     */
    public static boolean isHasPermission(@NonNull int... grantResult) {
        if (grantResult.length == 0) {
            return false;
        }
        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
