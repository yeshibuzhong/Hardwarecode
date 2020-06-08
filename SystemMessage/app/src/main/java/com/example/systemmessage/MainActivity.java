package com.example.systemmessage;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.mylistview);
        listview = (ListView)findViewById(R.id.mylistview);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,putData(),R.layout.list_item,
                new String[]{"name","age"},new int[]{R.id.title,R.id.value});
        listview.setAdapter(simpleAdapter);

    }

    public List<Map<String,Object>> putData(){

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

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
        list.add(createMap(new String[]{"Build c", ""}));
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
        list.add(createMap(new String[]{"Phone Number", getPhoneNum(this)}));
        list.add(createMap(new String[]{"IMEI", getIMEI(this)}));
        list.add(createMap(new String[]{"Sim Subscriber id", getSubscriberId(this)}));
        list.add(createMap(new String[]{"Sim Operator", getSimOperator(this)}));
        list.add(createMap(new String[]{"SIM 国家代码", getSimCountryIso(this)}));
        list.add(createMap(new String[]{"Sim Operator Name", getSimOperatorName(this)}));
        list.add(createMap(new String[]{"Sim Serial Number", getSimSerialNumber(this)}));
        list.add(createMap(new String[]{"Sim Status", String.valueOf(getSimState(this))}));
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
            /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
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
    public static String getIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method method = manager.getClass().getMethod("getImei", int.class);
            String imei1 = (String) method.invoke(manager, 0);
            String imei2 = (String) method.invoke(manager, 1);
            if(TextUtils.isEmpty(imei2)){
                return imei1;
            }
            if(!TextUtils.isEmpty(imei1)){
                //因为手机卡插在不同位置，获取到的imei1和imei2值会交换，所以取它们的最小值,保证拿到的imei都是同一个
                String imei = "";
                if(imei1.compareTo(imei2) <= 0){
                    imei = imei1;
                }else{
                    imei = imei2;
                }
                return imei;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return manager.getDeviceId();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    public static String getPhoneNum(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getLine1Number();//获取本机号码
    }

    @SuppressLint("MissingPermission")
    public static String getSubscriberId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSubscriberId();//获取本机号码
    }

    @SuppressLint("MissingPermission")
    public static String getSimOperator(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimOperator();//获取本机号码
    }

    @SuppressLint("MissingPermission")
    public static String getSimCountryIso(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimCountryIso();//获取本机号码
    }

    @SuppressLint("MissingPermission")
    public static String getSimOperatorName(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimOperatorName();//获取本机号码
    }

    @SuppressLint("MissingPermission")
    public static String getSimSerialNumber(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimSerialNumber();//获取本机号码
    }

    @SuppressLint("MissingPermission")
    public static int getSimState(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimState();//获取本机号码
    }

}