package com.devabit.takestock;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Victor Artemyev on 19/10/2016.
 */

public final class TakeStockPref {

    private static final String TAKESTOCK_PREF = "TAKESTOCK_PREF";
    private static final String KEY_FCM_TOKEN = "FCM_TOKEN"; // key for Firebase Cloud Messaging token
    private static final String KEY_DEVICE_REGISTERED = "DEVICE_REGISTERED";
    private static final String KEY_NOTIFICATION_ID = "NOTIFICATION_ID";

    public static void putFCMToken(Context context, String token) {
        getPreferences(context)
                .edit()
                .putString(KEY_FCM_TOKEN, token)
                .apply();
    }

    public static String getFCMToken(Context context) {
        return getPreferences(context)
                .getString(KEY_FCM_TOKEN, "");
    }

    public static void putIsDeviceRegistered(Context context, boolean isRegistered) {
        getPreferences(context)
                .edit()
                .putBoolean(KEY_DEVICE_REGISTERED, isRegistered)
                .apply();
    }

    public static boolean isDeviceRegistered(Context context) {
        return getPreferences(context)
                .getBoolean(KEY_DEVICE_REGISTERED, false);
    }

    public static int getNotificationId(Context context) {
        SharedPreferences pref = getPreferences(context);
        int id = pref.getInt(KEY_NOTIFICATION_ID, 0);
        pref.edit().putInt(KEY_NOTIFICATION_ID, ++id).apply();
        return id;
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(TAKESTOCK_PREF, Context.MODE_PRIVATE);
    }

    private TakeStockPref() {
        throw new AssertionError();
    }
}
