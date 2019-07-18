package com.lishang.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;

import com.lishang.permissions.listener.OnPermissionListener;
import com.lishang.permissions.listener.PermissionResult;

import java.util.ArrayList;
import java.util.List;


public class PermissionFragment extends Fragment {
    public static final String TAG = "PermissionFragment";

    private Context context;
    private static final int REQUESTCODE = 994;
    private OnPermissionListener listener;

    public void request(OnPermissionListener listener, @Size(min = 1) @NonNull String... perms) {
        this.listener = listener;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //sdk<19
            resultGranted();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //sdk<23
            int code = CheckPermissionCompat.checkSelfPermission(context, perms);
            PermissionResult result = new PermissionResult();
            if (code == AppOpsManagerCompat.MODE_ALLOWED) {
                result.granted = true;
            } else {
                result.shouldShowRequestPermissionRationale = true;
            }
            if (listener != null) {
                listener.onResult(result);
            }
        } else {
            boolean flag = true;
            for (String perm : perms) {
                if (ActivityCompat.checkSelfPermission(context, perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultGranted();
            } else {

                requestPermissions(perms, REQUESTCODE);

            }
        }
    }

    /**
     * 申请权限同意
     */
    private void resultGranted() {
        if (listener != null) {
            PermissionResult result = new PermissionResult();
            result.granted = true;
            listener.onResult(result);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUESTCODE) {

            List<String> granted = new ArrayList<>();
            List<String> denied = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted.add(perm);
                } else {
                    denied.add(perm);
                }
            }

            PermissionResult result = new PermissionResult();

            if (!denied.isEmpty()) {
                //拒绝
                result.shouldShowRequestPermissionRationale = true;
                for (String deniedPermission : denied) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, deniedPermission)) {
                        //勾选 不再提示
                        result.shouldShowRequestPermissionRationale = false;
                        break;
                    }
                }
            } else {
                //没有拒绝，默认通过
                result.granted = true;
            }

            if (listener != null) {
                listener.onResult(result);
            }

        }
    }


}
