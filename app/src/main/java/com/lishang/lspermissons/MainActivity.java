package com.lishang.lspermissons;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lishang.permissions.LSPermissions;
import com.lishang.permissions.dialog.PermissionDialog;
import com.lishang.permissions.listener.OnActionListener;
import com.lishang.permissions.listener.OnAllGrantedListener;
import com.lishang.permissions.listener.OnBeforeListener;
import com.lishang.permissions.listener.OnDeniedListener;
import com.lishang.permissions.listener.OnGrantedListener;
import com.lishang.permissions.request.Request;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void request(View view) {

        LSPermissions.with(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onBefore(new OnBeforeListener() {
                    @Override
                    public void onBefore(final Request request, String... perms) {
                        PermissionDialog.showApplyDialog(MainActivity.this, "缺失必要权限,是否申请?", new OnActionListener() {
                            @Override
                            public void onConfirm() {
                                request.execute();
                            }

                            @Override
                            public void onCancel() {
                                request.cancel();
                            }
                        });
                    }
                })
                .onAllGranted(new OnAllGrantedListener() {
                    @Override
                    public void onAllGranted() {
                        log("所以权限申请成功");
                        toast("所以权限申请成功");
                    }
                })
                .onGranted(new OnGrantedListener() {
                    @Override
                    public void onGranted(List<String> perms) {
                        log(perms.toString() + "权限申请成功");

                        toast(perms.toString() + "权限申请成功");
                    }
                })
                .onDenied(new OnDeniedListener() {
                    @Override
                    public void onDenied(final Request request, List<String> perms, List<String> permanentlyDeniedPerms) {
                        toast("被拒绝的权限：" + perms.toString() + "\n" + "永久拒绝的权限：" + permanentlyDeniedPerms.toString());

                        PermissionDialog.showSettingDialog(MainActivity.this, "权限被拒绝，请去设置里打开", new OnActionListener() {
                            @Override
                            public void onConfirm() {
                                request.setting();
                            }

                            @Override
                            public void onCancel() {
                                request.cancel();
                            }
                        });

                    }
                }).start();

    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String msg) {
        Log.e(TAG, msg);
    }

    public void request1(View view) {
        LSPermissions.with(this)
                .permissions(Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE)
                .onBefore(new OnBeforeListener() {
                    @Override
                    public void onBefore(final Request request, String... perms) {
                        PermissionDialog.showApplyDialog(MainActivity.this, "缺失必要权限,是否申请?", new OnActionListener() {
                            @Override
                            public void onConfirm() {
                                request.execute();
                            }

                            @Override
                            public void onCancel() {
                                request.cancel();
                            }
                        });
                    }
                })
                .onAllGranted(new OnAllGrantedListener() {
                    @Override
                    public void onAllGranted() {
                        log("所以权限申请成功");
                        toast("所以权限申请成功");
                    }
                })
                .onGranted(new OnGrantedListener() {
                    @Override
                    public void onGranted(List<String> perms) {
                        log(perms.toString() + "权限申请成功");

                        toast(perms.toString() + "权限申请成功");
                    }
                })
                .onDenied(new OnDeniedListener() {
                    @Override
                    public void onDenied(final Request request, List<String> perms, List<String> permanentlyDeniedPerms) {

                        toast("被拒绝的权限：" + perms.toString() + "\n" + "永久拒绝的权限：" + permanentlyDeniedPerms.toString());

                        PermissionDialog.showSettingDialog(MainActivity.this, "权限被拒绝，请去设置里打开", new OnActionListener() {
                            @Override
                            public void onConfirm() {
                                request.setting();
                            }

                            @Override
                            public void onCancel() {
                                request.cancel();
                            }
                        });

                    }
                })
                .start();
    }

}
