package com.devabit.takestock.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Victor Artemyev on 16/05/2016.
 */
public interface DateFormats {

    DateFormat API_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateFormat DEFAULT_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    DateFormat EXPIRY_FORMAT = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
}
