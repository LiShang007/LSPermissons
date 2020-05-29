package com.lishang.permissions.request;

import androidx.fragment.app.FragmentManager;

import com.lishang.permissions.PermissionFragment;
import com.lishang.permissions.listener.OnAllGrantedListener;
import com.lishang.permissions.listener.OnBeforeListener;
import com.lishang.permissions.listener.OnDeniedListener;
import com.lishang.permissions.listener.OnGrantedListener;
import com.lishang.permissions.listener.OnPermissionListener;


public class PermissionRequest implements Request, OnPermissionListener {

    public static final String TAG = "PermissionFragment";
    public static final int PERMISSION_REQUEST_CODE = 994;
    public static final int SETTING_REQUEST_CODE = 431;
    /**
     * 当前需要申请的权限
     */
    private String[] perms;
    /**
     * 当前被拒绝的权限
     */
    private String[] deniedPerms;

    /**
     * 申请的权限是否全部通过了
     */
    private OnAllGrantedListener onAllGrantedListener;
    /**
     * 同意的权限
     */
    private OnGrantedListener onGrantedListener;
    /**
     * 拒绝的权限
     */
    private OnDeniedListener onDeniedListener;


    /**
     * 权限申请之前
     */
    private OnBeforeListener onBeforeListener;
    /**
     * 权限申请页面
     */
    private PermissionFragment fragment;

    public PermissionRequest(FragmentManager fm) {
        fragment = findFragment(fm);
    }

    public void setPerms(String... perms) {
        this.perms = perms;
    }

    public void setOnAllGrantedListener(OnAllGrantedListener onAllGrantedListener) {
        this.onAllGrantedListener = onAllGrantedListener;
    }

    public void setOnGrantedListener(OnGrantedListener onGrantedListener) {
        this.onGrantedListener = onGrantedListener;
    }

    public void setOnDeniedListener(OnDeniedListener onDeniedListener) {
        this.onDeniedListener = onDeniedListener;
    }


    public void setOnBeforeListener(OnBeforeListener onBeforeListener) {
        this.onBeforeListener = onBeforeListener;
    }

    public void start() {
        fragment.request(this, perms);
    }


    private PermissionFragment findFragment(FragmentManager fm) {
        if (fm.findFragmentByTag(TAG) == null) {
            PermissionFragment fragment = new PermissionFragment();
            fm.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fm.executePendingTransactions();
            return fragment;
        } else {
            return (PermissionFragment) fm.findFragmentByTag(TAG);
        }
    }

    @Override
    public void execute() {
        fragment.requestPermissions(this.deniedPerms, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void cancel() {


    }

    @Override
    public void setting() {
        fragment.setting();
    }

    @Override
    public void onBefore(String[] perms) {
        deniedPerms = perms;
        if (onBeforeListener != null) {
            onBeforeListener.onBefore(this, perms);
        } else {
            this.execute();
        }
    }

    @Override
    public void onResult(PermissionResult result) {

        if (onAllGrantedListener != null && result.allGranted) {
            onAllGrantedListener.onAllGranted();
        }

        if (onGrantedListener != null && !result.grantedList.isEmpty()) {
            onGrantedListener.onGranted(result.grantedList);
        }


        if (onDeniedListener != null && (!result.deniedList.isEmpty() || !result.permanentlyDeniedList.isEmpty())) {
            onDeniedListener.onDenied(this, result.deniedList, result.permanentlyDeniedList);
        }

    }


}
