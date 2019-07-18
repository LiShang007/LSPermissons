package com.lishang.lspermissons;

import android.Manifest;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lishang.permissions.LSPermissions;
import com.lishang.permissions.listener.OnPermissionListener;
import com.lishang.permissions.listener.PermissionResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content, new TestFragment())
                .commitAllowingStateLoss();

    }

    public void request(View view) {
        LSPermissions.request(this, new OnPermissionListener() {
            @Override
            public void onResult(PermissionResult result) {
                String str = "";
                if (result.granted) {
                    str = "申请权限成功";
                } else if (result.shouldShowRequestPermissionRationale) {
                    str = "拒绝权限";
                } else {
                    str = "不再提示";
                }
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
