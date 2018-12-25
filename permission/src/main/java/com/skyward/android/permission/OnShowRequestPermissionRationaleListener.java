package com.skyward.android.permission;

/**
 * @author skyward
 * date: 2018/12/21
 * desc:
 */
public interface OnShowRequestPermissionRationaleListener {

    /**
     * 用户勾选了不再提示按钮
     */
    void selectUnShowDialog();

    /**
     * 用户还没有勾选不再提示按钮
     */
    void unSelect();
}
