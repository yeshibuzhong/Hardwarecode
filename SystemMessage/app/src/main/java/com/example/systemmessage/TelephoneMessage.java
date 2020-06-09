package com.example.systemmessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephoneMessage {

    String imei;
    String phoneNumber;
    String subscriberId;
    String simOperator;
    String simCountryIso;
    String simSerialNumber;
    String simState;
    String simOperatorName;

    @SuppressLint("MissingPermission")
    public static TelephoneMessage getPhoneMessage(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        TelephoneMessage message = new TelephoneMessage();
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

}
