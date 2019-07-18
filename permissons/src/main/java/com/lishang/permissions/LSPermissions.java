package com.lishang.permissions;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.lishang.permissions.listener.OnPermissionListener;

public class LSPermissions {

    public static void request(FragmentActivity activity, OnPermissionListener listener, @Size(min = 1) @NonNull String... perms) {
        request(activity.getSupportFragmentManager(), listener, perms);
    }

    public static void request(Fragment fragment, OnPermissionListener listener, @Size(min = 1) @NonNull String... perms) {
        request(fragment.getChildFragmentManager(), listener, perms);
    }


    private static void request(FragmentManager fm, OnPermissionListener listener, @Size(min = 1) @NonNull String... perms) {

        PermissionFragment fragment = findFragment(fm);

        fragment.request(listener, perms);
    }

    private static PermissionFragment findFragment(FragmentManager fm) {
        if (fm.findFragmentByTag(PermissionFragment.TAG) == null) {
            PermissionFragment fragment = new PermissionFragment();
            fm.beginTransaction()
                    .add(fragment, PermissionFragment.TAG)
                    .commitAllowingStateLoss();
            fm.executePendingTransactions();
            return fragment;
        } else {
            return (PermissionFragment) fm.findFragmentByTag(PermissionFragment.TAG);
        }
    }

}
