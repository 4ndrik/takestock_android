package com.devabit.takestock.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.devabit.takestock.utils.Logger.LOGE;

/**
 * Created by Victor Artemyev on 16/05/2016.
 */
public final class DateUtil {

    private static final String TAG = Logger.makeLogTag(DateUtil.class);

    public static final DateFormat API_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    public static final DateFormat EXPIRY_FORMAT = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

    public static String formatToApiDate(String value) {
        if (TextUtils.isEmpty(value)) return "";
        try {
            Date date = EXPIRY_FORMAT.parse(value);
            return API_FORMAT.format(date);
        } catch (ParseException e) {
            LOGE(TAG, "BOOM:", e);
            return "";
        }
    }

    public static String formatToExpiryDate(String value) {
        if (TextUtils.isEmpty(value)) return"";
        try {
            Date date = API_FORMAT.parse(value);
            return EXPIRY_FORMAT.format(date);
        } catch (ParseException e) {
            LOGE(TAG, "BOOM:", e);
            return "";
        }
    }

    public static String formatToDefaultDate(String value) {
        if (TextUtils.isEmpty(value)) return"";
        try {
            Date date = API_FORMAT.parse(value);
            return DEFAULT_FORMAT.format(date);
        } catch (ParseException e) {
            LOGE(TAG, "BOOM:", e);
            return "";
        }
    }
}
