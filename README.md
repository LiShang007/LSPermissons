## 一款简单的Android权限检测库
引用：
    
    implementation 'com.lishang:LSPermissions:1.0.0'
使用：


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


通过内部封装了一个Fragment用于权限申请，免于在每次使用时重写onRequestPermissionsResult，导致代码阅读不流畅。
SDK>=6.0 使用checkSelfPermission检测权限，SDK>=4.4使用AppOpsManager检测权限。

AppOpsManager是在SDK19时引入的，但是Google大佬并不建议开发者使用，里面的方法基本都被hide隐藏着，无法直接使用。

我在这里是通过一个映射，将权限映射为为对应的op

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

然后通过反射调用AppOpsManager.checkOp(int op,int uid,String packageName);
    
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




