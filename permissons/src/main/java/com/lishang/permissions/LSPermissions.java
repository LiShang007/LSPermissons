package com.lishang.permissions;


import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.lishang.permissions.listener.OnAllGrantedListener;
import com.lishang.permissions.listener.OnBeforeListener;
import com.lishang.permissions.listener.OnDeniedListener;
import com.lishang.permissions.listener.OnGrantedListener;
import com.lishang.permissions.request.PermissionRequest;


public class LSPermissions {

    private PermissionRequest request;


    private LSPermissions(FragmentManager fm) {
        request = new PermissionRequest(fm);
    }

    /**
     * 初始化
     *
     * @param activity
     * @return
     */
    public static LSPermissions with(FragmentActivity activity) {
        return new LSPermissions(activity.getSupportFragmentManager());
    }

    /**
     * 初始化
     *
     * @param fragment
     * @return
     */
    public static LSPermissions with(Fragment fragment) {
        return new LSPermissions(fragment.getChildFragmentManager());
    }

    /**
     * 申请的权限
     *
     * @param perms 需要申请的权限
     * @return
     */
    public LSPermissions permissions(@Size(min = 1) @NonNull String... perms) {
        this.request.setPerms(perms);
        return this;
    }

    /**
     * 监听申请的权限都允许了
     *
     * @param listener
     * @return
     */
    public LSPermissions onAllGranted(OnAllGrantedListener listener) {
        this.request.setOnAllGrantedListener(listener);
        return this;
    }

    /**
     * 同意的权限
     *
     * @param listener
     * @return
     */
    public LSPermissions onGranted(OnGrantedListener listener) {
        this.request.setOnGrantedListener(listener);
        return this;
    }

    /**
     * 拒绝的权限
     *
     * @param listener
     * @return
     */
    public LSPermissions onDenied(OnDeniedListener listener) {
        this.request.setOnDeniedListener(listener);
        return this;
    }


    /**
     * 申请权限之前拦截
     *
     * @return
     */
    public LSPermissions onBefore(OnBeforeListener listener) {
        this.request.setOnBeforeListener(listener);
        return this;
    }

    /**
     * 开始申请权限
     */
    public void start() {
        this.request.start();
    }


}
