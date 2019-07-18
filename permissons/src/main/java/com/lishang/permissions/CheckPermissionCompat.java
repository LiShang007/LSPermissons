package com.lishang.permissions;


import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.v4.app.AppOpsManagerCompat;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 检测4.4以上 6.0以下
 */
public class CheckPermissionCompat {

    private static String[] sOpPerms = new String[]{
            //calendar
            android.Manifest.permission.READ_CALENDAR,

            android.Manifest.permission.WRITE_CALENDAR,

            //CAMERA
            android.Manifest.permission.CAMERA,
            //ONTACTS
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.GET_ACCOUNTS,
            //LOCATION
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            //AUDIO
            android.Manifest.permission.RECORD_AUDIO,
            //PHONE
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.WRITE_CALL_LOG,
            android.Manifest.permission.ADD_VOICEMAIL,
            android.Manifest.permission.USE_SIP,
            android.Manifest.permission.PROCESS_OUTGOING_CALLS,
            //BODY_SENSORS
            android.Manifest.permission.BODY_SENSORS,
            //SMS
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.RECEIVE_WAP_PUSH,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.RECEIVE_MMS,
            //STORAGE
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    private static Integer[] sOpToCode = new Integer[]{
            //calendar
            8, // android.Manifest.permission.READ_CALENDAR,"android:read_calendar",//
            9, //android.Manifest.permission.WRITE_CALENDAR, "android:write_calendar",//

            //CAMERA
            26,//  android.Manifest.permission.CAMERA,  "android:camera",//
            //ONTACTS，
            4,// android.Manifest.permission.READ_CONTACTS, "android:read_contacts",//
            5,//android.Manifest.permission.WRITE_CONTACTS, "android:write_contacts",//
            62,//android.Manifest.permission.GET_ACCOUNTS,"android:get_accounts",//
            //LOCATION
            0,// android.Manifest.permission.ACCESS_COARSE_LOCATION,  "android:coarse_location",//
            1,//android.Manifest.permission.ACCESS_FINE_LOCATION,"android:fine_location",//
            //AUDIO
            27,// android.Manifest.permission.RECORD_AUDIO,"android:record_audio",//
            //PHONE
            51,//  Manifest.permission.READ_PHONE_STATE, "android:read_phone_state",//
            13,//  android.Manifest.permission.CALL_PHONE, "android:call_phone",//
            6,//  android.Manifest.permission.READ_CALL_LOG,  "android:read_call_log",//
            7,//android.Manifest.permission.WRITE_CALL_LOG, "android:write_call_log",//
            52,//  Manifest.permission.ADD_VOICEMAIL,   "android:add_voicemail",//
            53,//  Manifest.permission.USE_SIP,    "android:use_sip",//
            54,//  Manifest.permission.PROCESS_OUTGOING_CALLS,   "android:process_outgoing_calls",//
            //BODY_SENSORS
            56,//  Manifest.permission.BODY_SENSORS,"android:body_sensors",//
            //SMS
            14,//   android.Manifest.permission.READ_SMS, "android:read_sms",//
            16,// android.Manifest.permission.RECEIVE_SMS, "android:receive_sms",//
            19,//  android.Manifest.permission.RECEIVE_WAP_PUSH, "android:receive_wap_push",//
            20,//  android.Manifest.permission.SEND_SMS, "android:send_sms",//
            18,//  Manifest.permission.RECEIVE_MMS,   "android:receive_mms",//
            //STORAGE
            59,// Manifest.permission.READ_EXTERNAL_STORAGE, "android:read_external_storage",//
            60,//Manifest.permission.WRITE_EXTERNAL_STORAGE,  "android:write_external_storage",//
    };

    private static Map<String, Integer> map = new HashMap<>();

    static {
        int len = sOpPerms.length;
        for (int i = 0; i < len; i++) {
            map.put(sOpPerms[i], sOpToCode[i]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int checkSelfPermission(Context context, String perm) {
        Integer code = map.get(perm);
        if (code == null) return AppOpsManagerCompat.MODE_ALLOWED; //默认有权限
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsManager != null) {

            try {
                Method method = AppOpsManager.class.getDeclaredMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int checkOp = (int) method.invoke(appOpsManager, code, Binder.getCallingUid(), context.getPackageName());
                return checkOp;
            } catch (Exception e) {
                e.printStackTrace();
                //报错说明当前版本sdk AppOpsManager 没有对应权限
                return AppOpsManagerCompat.MODE_ALLOWED; //默认有权限
            }
        } else {
            return AppOpsManagerCompat.MODE_ALLOWED; //默认有权限
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int checkSelfPermission(Context context, @Size(min = 1) @NonNull String... perms) {
        for (String perm : perms) {
            int code = checkSelfPermission(context, perm);
            if (code != AppOpsManagerCompat.MODE_ALLOWED) {
                return AppOpsManagerCompat.MODE_IGNORED;
            }
        }
        return AppOpsManagerCompat.MODE_ALLOWED;
    }

}
