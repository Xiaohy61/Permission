package com.skyward.android.permission;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;


/**
 * @author skyward
 */
public class PermissionDialogActivity extends AppCompatActivity {

    private static final String PERMISSIONS = "permissions";


    private static final int PERMISSION_REQUEST_CODE = 64;
    private String[] permissions;

    private RelativeLayout tipLayout;
    private static OnPermissionListener mListener;
    private boolean require = true;
    String[] unGetPermissions;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_permission_dialog);
        permissions = new String[1];
        permissions = getIntent().getStringArrayExtra(PERMISSIONS);
        initView();
    }


    /**
     * 权限监听回调
     *
     * @param listener listener
     */
    public static void onPermissionListener(OnPermissionListener listener) {
        mListener = listener;
    }


    protected void initView() {

        tipLayout = findViewById(R.id.tip_layout);
        TextView btnCancel = findViewById(R.id.btn_cancel);
        TextView tip = findViewById(R.id.tip_content2);
        TextView btnSure = findViewById(R.id.btn_sure);
        tip.setText("请点击：\"设置\"-\"权限\"-打开所需权限。");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancelClick();
                }

                finish();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetting(PermissionDialogActivity.this);
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();


        int unGetPermissionCount = 0;

        for (String permission : permissions) {

            if (PermissionChecker.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                unGetPermissionCount++;
            }


        }

        unGetPermissions = new String[unGetPermissionCount];
        int index = 0;

        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                unGetPermissions[index] = permission;
                index++;
            }
        }


        //有些手机，比如小米，用户手动设置了允许权限，其实还没有真正获取到授权，要再次请求才可以获取到
        if (require) {
            tipLayout.setVisibility(View.GONE);
            if (RequestPermission.hasPermission(new WeakReference<Context>(this), unGetPermissions)) {
                finish();
                if (mListener != null) {
                    mListener.onPermissionSuccess();
                }
            } else {
                //请求申请权限,这个方法会触发onResume
                requestPermissions(unGetPermissions, PERMISSION_REQUEST_CODE);
                require = false;
            }
        } else {
            if (RequestPermission.hasPermission(new WeakReference<Context>(this), unGetPermissions)) {
                finish();
                if (mListener != null) {
                    mListener.onPermissionSuccess();
                }
            } else {
                if (mListener != null) {
                    mListener.onPermissionFailure(unGetPermissions);
                }
                require = true;
            }

        }

    }


    /**
     * 去设置，用户手动设置授权
     *
     * @param context 上下文
     */
    private void gotoSetting(@NonNull Context context) {
        tipLayout.setVisibility(View.GONE);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE && isHasPermission(grantResults) && RequestPermission.hasPermission(new WeakReference<Context>(this), permissions)) {
            tipLayout.setVisibility(View.GONE);
            if (mListener != null) {
                mListener.onPermissionSuccess();
            }
        } else {

            //如果授权失败，把弹窗显示出来让用户手动设置授权
            tipLayout.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 判断一组授权结果是否为授权通过
     *
     * @param grantResult grantResult
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestPermission.isStartActivity = false;
        if(mListener != null){
            mListener = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
