package com.lishang.lspermissons;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lishang.permissions.LSPermissions;
import com.lishang.permissions.listener.OnPermissionListener;
import com.lishang.permissions.listener.PermissionResult;

public class TestFragment extends Fragment implements View.OnClickListener {
    private View view;
    /**
     * 申请权限
     */
    private Button mBtn;
    /**
     * READ_PHONE_STATE
     */
    private Button mReadPhoneState;
    /**
     * CAMERA
     */
    private Button mCamera;
    /**
     * READ_CONTACTS
     */
    private Button mReadContacts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getActivity(), R.layout.fragment_layout, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO:OnCreateView Method has been created, run FindViewById again to generate code
        initView(view);
        return view;
    }

    public void initView(View view) {
        mBtn = (Button) view.findViewById(R.id.btn);
        mBtn.setOnClickListener(this);
        mReadPhoneState = (Button) view.findViewById(R.id.read_phone_state);
        mReadPhoneState.setOnClickListener(this);
        mCamera = (Button) view.findViewById(R.id.camera);
        mCamera.setOnClickListener(this);
        mReadContacts = (Button) view.findViewById(R.id.read_contacts);
        mReadContacts.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String perm = "";
        switch (v.getId()) {
            default:
                break;
            case R.id.btn:
                perm = Manifest.permission.CALL_PHONE;
                break;
            case R.id.read_phone_state:
                perm = Manifest.permission.READ_PHONE_STATE;
                break;
            case R.id.camera:
                perm = Manifest.permission.CAMERA;
                break;
            case R.id.read_contacts:
                perm = Manifest.permission.READ_CONTACTS;
                break;
        }
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
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            }
        }, new String[]{perm});

    }
}
