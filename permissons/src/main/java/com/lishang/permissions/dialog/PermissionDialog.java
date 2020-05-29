package com.lishang.permissions.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.lishang.permissions.listener.OnActionListener;

public class PermissionDialog {

    public static void show(Context context, String msg, String confirm, String cancel, final OnActionListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onCancel();
                        }
                    }
                })
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showApplyDialog(Context context, String msg, OnActionListener listener) {
        show(context, msg, "申请", "取消", listener);
    }

    public static void showSettingDialog(Context context, String msg, OnActionListener listener) {
        show(context, msg, "设置", "取消", listener);
    }


}
