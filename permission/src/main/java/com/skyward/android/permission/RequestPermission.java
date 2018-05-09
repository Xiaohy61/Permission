package com.skyward.android.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;

/**
 * @author: skyward
 * date: 2018/5/8
 * desc: 申请授权
 */
public class RequestPermission {


    public static void request(Context context, OnPermissionListener listener, String... permissions) {


        if(hasPermission(context,permissions)){
            listener.onPermissionSuccess();
        }else {
            PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(permissions);
            dialogFragment.onPermissionListener(listener);
            dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");
        }





    }

    /**
     * 是否拥有了申请的权限
     * @param context
     * @param permissions
     * @return
     */
    public static  boolean hasPermission(Context context,String... permissions){
        for (String permission: permissions) {
            if(PermissionChecker.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}
