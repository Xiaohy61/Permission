package com.skyward.android.permission;

/**
 * @author: skyward
 * date: 2018/5/8
 * desc:
 */
public interface OnPermissionListener {

    /**
     * 权限申请成功回调
     */
    void onPermissionSuccess();

    /**
     * 权限申请失败回调
     * @param permission 申请的权限
     */
    void onPermissionFailure(String[] permission);

    /**
     * 点击dialog取消回调
     */
    void onCancelClick();
}
