package com.lishang.permissions.request;

import java.util.ArrayList;
import java.util.List;

public class PermissionResult {
    /**
     * 用户同意了所有申请权限
     **/
    public boolean allGranted;

    /**
     * 用户同意的权限列表
     */
    public List<String> grantedList = new ArrayList<>();

    /**
     * 用户拒绝的权限列表
     */
    public List<String> deniedList = new ArrayList<>();

    /**
     * 勾选了 不再提示的权限列表
     */
    public List<String> permanentlyDeniedList = new ArrayList<>();

}
