package com.example.systemmessage;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Adapter;

import androidx.annotation.RequiresApi;

import java.util.Iterator;
import java.util.Set;

public class BluetoothMessage {

    public static class BluetoothMessageInfo
    {
        String name;
        String macAddress;
    }

    private static String TAG = "BluetoothMessage";

    /*

    * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static BluetoothMessageInfo getBluetoothMessage(Context context)
    {
        BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bleAdapter = bluetoothManager.getAdapter();

        BluetoothMessageInfo info = new  BluetoothMessageInfo();
        info.name = bleAdapter.getName();
        info.macAddress = bleAdapter.getAddress();

        Log.d(TAG, "getBluetoothMessage: " + info.name + "macAddress: " + info.macAddress);

        return info;
    }
}
