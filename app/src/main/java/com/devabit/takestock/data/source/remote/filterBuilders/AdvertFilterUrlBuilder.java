package com.devabit.takestock.data.source.remote.filterBuilders;

import com.devabit.takestock.data.filters.AdvertFilter;

import static com.devabit.takestock.data.filters.AdvertFilter.*;
import static com.devabit.takestock.rest.RestApi.ADVERTS;

/**
 * Created by Victor Artemyev on 20/05/2016.
 */
public class AdvertFilterUrlBuilder {

    private static final String ITEMS_COUNT = "items_count";
    private static final String CATEGORY = "category";
    private static final String AUTHOR_ID = "author_id";
    private static final String ORDER = "o";

    public static final String EXPIRES_AT = "expires_at";
    public static final String EXPIRES_AT_DESCENDING = "-expires_at";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_AT_DESCENDING = "-created_at";
    public static final String GUIDE_PRICE = "guide_price";
    public static final String GUIDE_PRICE_DESCENDING = "-guide_price";

    private final StringBuilder mBuilder;

    public AdvertFilterUrlBuilder() {
        mBuilder = new StringBuilder();
        mBuilder.append(ADVERTS);
    }

    public String buildUrl(AdvertFilter filter) {
        int itemCount = filter.getItemCount();
        if (itemCount > 0) {
            appendQueryParameter(ITEMS_COUNT, String.valueOf(itemCount));
        }
        int order = filter.getOrder();
        if (order > 0) {
            appendQueryParameter(ORDER, getOrderAsString(order));
        }
        int userId = filter.getAuthorId();
        if (userId > 0) {
            appendQueryParameter(AUTHOR_ID, String.valueOf(userId));
        }

        return mBuilder.toString();
    }

    private void appendQueryParameter(String key, String value) {
        mBuilder.append(mBuilder.charAt(mBuilder.length() - 1) == '/' ? '?' : '&');
        mBuilder.append(key);
        mBuilder.append('=');
        mBuilder.append(value);
    }

    private String getOrderAsString(int order) {
        switch (order) {
            case ORDER_EXPIRES_AT:
                return EXPIRES_AT;

            case ORDER_EXPIRES_AT_DESCENDING:
                return EXPIRES_AT_DESCENDING;

            case ORDER_CREATED_AT:
                return CREATED_AT;

            case ORDER_CREATED_AT_DESCENDING:
                return CREATED_AT_DESCENDING;

            case ORDER_GUIDE_PRICE:
                return GUIDE_PRICE;

            case ORDER_GUIDE_PRICE_DESCENDING:
                return GUIDE_PRICE_DESCENDING;

            default:
                return "";
        }
    }
}
