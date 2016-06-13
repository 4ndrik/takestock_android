package com.devabit.takestock.utils;

import android.util.Log;
import com.devabit.takestock.BuildConfig;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public final class Logger {

    private static final String LOG_PREFIX = "takestock_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static boolean LOGGING_ENABLED = BuildConfig.LOGGING_ENABLED;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message);
        }
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message, cause);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.w(tag, message);
        }
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.w(tag, message, cause);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.e(tag, message);
        }
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.e(tag, message, cause);
        }
    }

    private Logger() {
        throw new AssertionError();
    }
}
