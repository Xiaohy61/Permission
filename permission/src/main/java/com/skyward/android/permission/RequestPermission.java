package com.skyward.android.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;

/**
 * @author: skyward
 * date: 2018/5/8
 * desc: 申请授权
 */
public class RequestPermission {


    public static void request(@NonNull Context context,@NonNull OnPermissionListener listener,@NonNull String... permissions) {


        if (hasPermission(context, permissions)) {
            listener.onPermissionSuccess();

        } else {

            /**
             * 未取得权限的数量统计
             */
            int count = 0;

            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    count++;
                }
            }
            /**
             * 把未取得权限的重新装箱去请求，目的：以防已请求过的权限多次请求
             */
            String[] unGetPermissions = new String[count];
            int index = 0;

            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    unGetPermissions[index] = permission;
                    index++;
                }
            }


            PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(unGetPermissions);
            dialogFragment.onPermissionListener(listener);
            dialogFragment.setActivityContext(context);
            dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");


        }

    }

    /**
     * 是否拥有了申请的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(Context context, String... permissions) {
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
