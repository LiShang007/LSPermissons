package com.lishang.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.app.ActivityCompat;
import androidx.core.app.AppOpsManagerCompat;
import androidx.fragment.app.Fragment;

import com.lishang.permissions.compat.CheckPermissionCompat;
import com.lishang.permissions.listener.OnPermissionListener;
import com.lishang.permissions.request.PermissionRequest;
import com.lishang.permissions.request.PermissionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PermissionFragment extends Fragment {

    private Context context;
    private OnPermissionListener listener;

    private PermissionResult result = new PermissionResult();

    private void reset() {
        result.allGranted = false;
        result.grantedList.clear();
        result.deniedList.clear();
        result.permanentlyDeniedList.clear();
    }

    public void request(OnPermissionListener listener, @Size(min = 1) @NonNull String... perms) {
        this.listener = listener;

        reset();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //sdk<19 默认全部允许
            if (listener != null) {
                result.allGranted = true;
                result.grantedList = Arrays.asList(perms);
                listener.onResult(result);
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //sdk<23

            result.allGranted = true;
            for (String perm : perms) {
                int code = CheckPermissionCompat.checkSelfPermission(context, perm);
                if (code != AppOpsManagerCompat.MODE_ALLOWED) {
                    result.allGranted = false;
                    result.permanentlyDeniedList.add(perm);
                } else {
                    result.grantedList.add(perm);
                }
            }
            if (listener != null) {
                listener.onResult(result);
            }

        } else {
            //sdk >= 23
            result.allGranted = true;
            for (String perm : perms) {
                if (ActivityCompat.checkSelfPermission(context, perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    result.allGranted = false;

                    result.deniedList.add(perm);

                } else {
                    result.grantedList.add(perm);
                }
            }
            if (result.allGranted) {
                if (listener != null) {
                    listener.onResult(result);
                }
            } else {
                //申请被拒绝的权限

                String[] denied = result.deniedList.toArray(new String[]{});
                if (listener != null) {
                    listener.onBefore(denied);
                } else {
                    requestPermissions(denied, PermissionRequest.PERMISSION_REQUEST_CODE);
                }
            }
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setting() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, PermissionRequest.SETTING_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PermissionRequest.PERMISSION_REQUEST_CODE) {
            result.allGranted = true;
            result.deniedList.clear();
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    result.grantedList.add(perm);
                } else {
                    result.allGranted = false;

                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, perm)) {
                        //勾选 不再提示
                        result.permanentlyDeniedList.add(perm);
                    } else {
                        result.deniedList.add(perm);
                    }
                }
            }

            if (listener != null) {
                listener.onResult(result);
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionRequest.SETTING_REQUEST_CODE) {
            List<String> perms = new ArrayList<>(result.deniedList);
            perms.addAll(result.permanentlyDeniedList);
            request(listener, perms.toArray(new String[]{}));
        }
    }
}
