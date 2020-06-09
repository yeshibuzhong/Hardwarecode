package com.example.systemmessage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class WIFIMessage {

    public static class WIFIMessageInfo
    {
        String name;
        String macAddress;
        String bssid;
        String ipAddress;
    }

    private static String TAG = "WIFIMessage";

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
     * 获取当前连接的wifi名称
     *
     * @param context
     * @return
     */
    public static WIFIMessageInfo getWIFIInfo(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();

        WIFIMessageInfo info1 = new  WIFIMessageInfo();

        //获取wifi名称
        info1.name = info != null ? info.getSSID().replace("\"", "") : null;
        Log.d(TAG, "ssid: " + info1.name);

        //获取mac地址
        info1.macAddress = info.getMacAddress();
        Log.d(TAG, "macAddress: " + info1.macAddress);

        //获取当前连接速度
        int linkSpeed = info.getLinkSpeed();
        Log.d(TAG, "linkSpeed: " + linkSpeed);

        //获取ip地址
        info1.ipAddress = String.valueOf(info.getIpAddress());
        Log.d(TAG, "ipAddress: " + info1.ipAddress);

        //获取bssid
        info1.bssid = info.getBSSID();
        Log.d(TAG, "bssid: " + info1.bssid);

        return info1;

    }


}
