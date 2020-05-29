package com.lishang.permissions.listener;

import com.lishang.permissions.request.Request;

import java.util.List;

/**
 * 拒绝的权限
 */
public interface OnDeniedListener {
    void onDenied(Request request,List<String> perms,List<String> permanentlyDeniedPerms);
}
