package com.example.systemmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity<permissionCheck> extends AppCompatActivity {

    private static class PhoneMessage
    {
        String imei;
        String phoneNumber;
        String subscriberId;
        String simOperator;
        String simCountryIso;
        String simSerialNumber;
        String simState;
        String simOperatorName;
    }


    String TAG = "MainActivity";
    private ListView listview;
    private static final String TODO = null;
    final int MY_PERMIEAD_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TelephonyManager Phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //获取权限状态
        int result =
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
        //判断权限是否开启
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMIEAD_CONTACTS);
        } else {
            setContentView(R.layout.mylistview);
            listview = (ListView)findViewById(R.id.mylistview);

            SimpleAdapter simpleAdapter = new SimpleAdapter(this,putData(),R.layout.list_item,
                    new String[]{"name","age"},new int[]{R.id.title,R.id.value});
            listview.setAdapter(simpleAdapter);

        }

    }

    public List<Map<String,Object>> putData(){

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        PhoneMessage message = getPhoneMessage(this);

        list.add(createMap(new String[]{"Build Manufacturer", android.os.Build.MANUFACTURER}));
        list.add(createMap(new String[]{"Build Product Name", android.os.Build.DEVICE}));
        list.add(createMap(new String[]{"Build Product brand", android.os.Build.BRAND}));
        list.add(createMap(new String[]{"Build Product board", android.os.Build.BOARD}));
        list.add(createMap(new String[]{"Build Model", Build.MODEL}));
        list.add(createMap(new String[]{"Build Product device", ""}));
        list.add(createMap(new String[]{"Build hardware", android.os.Build.HARDWARE}));
        list.add(createMap(new String[]{"Build Serial", Build.SERIAL}));
        list.add(createMap(new String[]{"Build Version Name", getAppVersionName(this)}));
        list.add(createMap(new String[]{"Build Sdk api", Build.VERSION.SDK}));
        list.add(createMap(new String[]{"Version CodeName", android.os.Build.VERSION.CODENAME}));
        list.add(createMap(new String[]{"Build Host", ""}));
        list.add(createMap(new String[]{"Version incremental", android.os.Build.VERSION.INCREMENTAL}));
        list.add(createMap(new String[]{"Build date", ""}));
        list.add(createMap(new String[]{"Display id", Build.DISPLAY}));
        list.add(createMap(new String[]{"Build id", Build.ID}));
        list.add(createMap(new String[]{"Bootloader", android.os.Build.BOOTLOADER}));
        list.add(createMap(new String[]{"Build fingerprint", Build.FINGERPRINT}));
        list.add(createMap(new String[]{"Build description", ""}));
        list.add(createMap(new String[]{"Build User Agent", Build.USER}));
        list.add(createMap(new String[]{"Android id", getAndroidId(this)}));
        list.add(createMap(new String[]{"gsm.version.baseband", ""}));
        list.add(createMap(new String[]{"Phone Number", message.phoneNumber}));
        list.add(createMap(new String[]{"IMEI", message.imei}));
        list.add(createMap(new String[]{"Sim Subscriber id", message.subscriberId}));
        list.add(createMap(new String[]{"Sim Operator", message.simOperator}));
        list.add(createMap(new String[]{"SIM 国家代码", message.simCountryIso}));
        list.add(createMap(new String[]{"Sim Operator Name", message.simOperatorName}));
        list.add(createMap(new String[]{"Sim Serial Number", message.simSerialNumber}));
        list.add(createMap(new String[]{"Sim Status", message.simState}));
        list.add(createMap(new String[]{"网络类型", getNetworkState(this)}));
        list.add(createMap(new String[]{"Wifi Name", ""}));
        list.add(createMap(new String[]{"Wifi BSSID", ""}));
        list.add(createMap(new String[]{"Wifi Mac Address", ""}));
        list.add(createMap(new String[]{"Bluetooth Name", ""}));
        list.add(createMap(new String[]{"Bluetooth Mac Address", ""}));
        list.add(createMap(new String[]{"Netip", ""}));
        list.add(createMap(new String[]{"Latitude", ""}));
        list.add(createMap(new String[]{"Longitude", ""}));
        list.add(createMap(new String[]{"IsRootClock", ""}));
        list.add(createMap(new String[]{"CPU info path", android.os.Build.CPU_ABI}));
        list.add(createMap(new String[]{"语言", ""}));
        list.add(createMap(new String[]{"DPI", ""}));


        return list;
    }

    private Map<String,Object> createMap(String[] strings)
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", (String)strings[0]);
        map.put("age", strings[1]);
        return  map;
    }

    public static String getAndroidId (Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }

    public static final String NETWORK_NONE = "无网"; // 没有网络连接
    public static final String NETWORK_WIFI = "WIFI"; // wifi连接
    public static final String NETWORK_2G = "2G"; // 2G
    public static final String NETWORK_3G = "3G"; // 3G
    public static final String NETWORK_4G = "4G"; // 4G
    public static final String NETWORK_MOBILE = "流量"; // 手机流量

    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return NETWORK_NONE;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_2G;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_4G;
            default:
                return NETWORK_MOBILE;
        }
    }

    /**
     * 获取当前app version name
     */
    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }

    /* 获取当前app version code
     */
    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    @SuppressLint("MissingPermission")
    public static PhoneMessage getPhoneMessage(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneMessage message = new PhoneMessage();
        message.phoneNumber = manager.getLine1Number();
        message.imei = manager.getDeviceId();
        message.subscriberId = manager.getSubscriberId();
        message.simOperator = manager.getSimOperator();
        message.simCountryIso = manager.getSimCountryIso();
        message.simOperatorName = manager.getSimOperatorName();
        message.simSerialNumber = manager.getSimSerialNumber();
        message.simState = String.valueOf(manager.getSimState());
        return message;
    }


    /**
     * 通过网络接口取
     * 获取wifiMac地址
     *
     * @return
     */
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}