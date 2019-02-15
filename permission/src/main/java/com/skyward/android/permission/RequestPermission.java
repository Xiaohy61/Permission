package com.skyward.android.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

import java.lang.ref.WeakReference;

/**
 * @author  skyward
 * date: 2018/5/8
 * desc: 申请授权
 */
public class RequestPermission {

    static boolean isStartActivity;


    public static void request(@NonNull WeakReference<Context> context, @NonNull OnPermissionListener listener, @NonNull String... permissions) {


        if (hasPermission(context, permissions)) {
            listener.onPermissionSuccess();

        } else {
            //在PermissionDialogActivity没有销毁之前，避免多次创建PermissionDialogActivity
            if (!isStartActivity) {
                Intent intent = new Intent(context.get(), PermissionDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("permissions", permissions);
                PermissionDialogActivity.onPermissionListener(listener);
                context.get().startActivity(intent);
                isStartActivity = true;
            }


        }

    }

    /**
     * 是否拥有了申请的权限
     *
     * @param context  context
     * @param permissions 申请的权限
     * @return bool
     */
    static boolean hasPermission(WeakReference<Context> context, String... permissions) {
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context.get(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
