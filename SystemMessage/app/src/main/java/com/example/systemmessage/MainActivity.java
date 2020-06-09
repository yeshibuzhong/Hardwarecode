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
import android.net.wifi.WifiInfo;
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

    String TAG = "MainActivity";
    private ListView listview;
    private static final String TODO = null;
    final int MY_PERMIEAD_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllMessage();

//        String userAgent = System.getProperty("http.agent");
//        Log.d(TAG, "onCreate: " + userAgent);
    }

    private void getAllMessage()
    {
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

        TelephoneMessage message = TelephoneMessage.getPhoneMessage(this);
        WIFIMessage.WIFIMessageInfo wifiInfo = WIFIMessage.getWIFIInfo(this);
        BluetoothMessage.BluetoothMessageInfo bluetoothMessageInfo = BluetoothMessage.getBluetoothMessage(this);

        list.add(createMap(new String[]{"Build Manufacturer", android.os.Build.MANUFACTURER}));
        list.add(createMap(new String[]{"Build Product Name", android.os.Build.DEVICE}));
        list.add(createMap(new String[]{"Build Product brand", android.os.Build.BRAND}));
        list.add(createMap(new String[]{"Build Product board", android.os.Build.BOARD}));
        list.add(createMap(new String[]{"Build Model", Build.MODEL}));
        list.add(createMap(new String[]{"Build Product device", Build.DEVICE}));
        list.add(createMap(new String[]{"Build hardware", Build.HARDWARE}));
        list.add(createMap(new String[]{"Build Serial", Build.SERIAL}));
        list.add(createMap(new String[]{"Build Version Name", getAppVersionName(this)}));
        list.add(createMap(new String[]{"Build Sdk api", String.valueOf(Build.VERSION.SDK_INT)}));
        list.add(createMap(new String[]{"Version CodeName", android.os.Build.VERSION.CODENAME}));
        list.add(createMap(new String[]{"Build Host", Build.HOST}));
        list.add(createMap(new String[]{"Version incremental", android.os.Build.VERSION.INCREMENTAL}));
        list.add(createMap(new String[]{"Build date", SystemProperties.get("ro.build.date")}));
        list.add(createMap(new String[]{"Display id", Build.DISPLAY}));
        list.add(createMap(new String[]{"Build id", Build.ID}));
        list.add(createMap(new String[]{"Bootloader", android.os.Build.BOOTLOADER}));
        list.add(createMap(new String[]{"Build fingerprint", Build.FINGERPRINT}));
        list.add(createMap(new String[]{"Build description", ""}));
        list.add(createMap(new String[]{"Build User Agent", System.getProperty("http.agent")}));
        list.add(createMap(new String[]{"Android id", getAndroidId(this)}));
        list.add(createMap(new String[]{"gsm.version.baseband", SystemProperties.get("gsm.version.baseband")}));
        list.add(createMap(new String[]{"Phone Number", message.phoneNumber}));
        list.add(createMap(new String[]{"IMEI", message.imei}));
        list.add(createMap(new String[]{"Sim Subscriber id", message.subscriberId}));
        list.add(createMap(new String[]{"Sim Operator", message.simOperator}));
        list.add(createMap(new String[]{"SIM 国家代码", message.simCountryIso}));
        list.add(createMap(new String[]{"Sim Operator Name", message.simOperatorName}));
        list.add(createMap(new String[]{"Sim Serial Number", message.simSerialNumber}));
        list.add(createMap(new String[]{"Sim Status", message.simState}));
        list.add(createMap(new String[]{"网络类型", WIFIMessage.getNetworkState(this)}));
        list.add(createMap(new String[]{"Wifi Name", wifiInfo.name}));
        list.add(createMap(new String[]{"Wifi BSSID", wifiInfo.bssid}));
        list.add(createMap(new String[]{"Wifi Mac Address", wifiInfo.macAddress}));
        list.add(createMap(new String[]{"Bluetooth Name", bluetoothMessageInfo.name}));
        list.add(createMap(new String[]{"Bluetooth Mac Address", bluetoothMessageInfo.macAddress}));
        list.add(createMap(new String[]{"Netip", String.valueOf(wifiInfo.ipAddress)}));
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