package com.lishang.permissions.listener;

import com.lishang.permissions.request.PermissionResult;

public interface OnPermissionListener {

    void onBefore(String... perms);

    void onResult(PermissionResult result);
}
