package com.lishang.permissions.listener;

import com.lishang.permissions.request.Request;

/**
 * 权限申请之前
 */
public interface OnBeforeListener {
    void onBefore(Request request, String... perms);
}
