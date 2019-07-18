package com.lishang.permissions.listener;

public class PermissionResult {
    /**
     * 用户同意了该权限
     **/
    public boolean granted;
    /**
     * 用户拒绝了该权限，没有选择 不再询问
     **/
    public boolean shouldShowRequestPermissionRationale;

}
