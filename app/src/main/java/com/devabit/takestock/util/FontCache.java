package com.devabit.takestock.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public final class FontCache {

    private static Map<String, Typeface> sCachedFonts = new HashMap<>();

    public static Typeface getTypeface(Context context, String assetPath) {
        if (!sCachedFonts.containsKey(assetPath)) {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), assetPath);
            sCachedFonts.put(assetPath, tf);
        }

        return sCachedFonts.get(assetPath);
    }

    public  static Typeface getTypeface(Context context, @StringRes int resId) {
        return getTypeface(context, context.getString(resId));
    }
}
