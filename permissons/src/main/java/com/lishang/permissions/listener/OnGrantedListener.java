package com.lishang.permissions.listener;

import java.util.List;

/**
 * 同意的权限
 */
public interface OnGrantedListener {
    void onGranted(List<String> perms);
}
