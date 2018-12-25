package com.skyward.android.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

/**
 * @author: skyward
 * date: 2018/5/8
 * desc: 申请授权
 */
public class RequestPermission {

    public static boolean  isStartActivity;

    public static void request(@NonNull Context context, @NonNull OnPermissionListener listener, @NonNull String... permissions) {


        if (hasPermission(context, permissions)) {
            listener.onPermissionSuccess();

        } else {

            if(!isStartActivity){
                Intent intent = new Intent(context,PermissionDialogActivity.class);
                intent.putExtra("permissions", permissions);
                PermissionDialogActivity.onPermissionListener(listener);
                context.startActivity(intent);
                isStartActivity = true;
            }


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
