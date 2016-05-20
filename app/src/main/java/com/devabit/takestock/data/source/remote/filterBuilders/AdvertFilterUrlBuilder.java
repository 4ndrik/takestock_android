package com.devabit.takestock.data.source.remote.filterBuilders;

import android.net.Uri;
import com.devabit.takestock.data.filters.AdvertFilter;

import static com.devabit.takestock.data.filters.AdvertFilter.*;
import static com.devabit.takestock.rest.RestApi.API_BASE_URL;
import static com.devabit.takestock.rest.RestApi.GET_ADVERTS;
import static com.devabit.takestock.rest.RestApi.SCHEME;

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

    public String buildUrl(AdvertFilter filter) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME);
        builder.encodedAuthority(API_BASE_URL);
        builder.appendEncodedPath(GET_ADVERTS);
        int itemCount = filter.getItemCount();
        if (itemCount > 0) {
            builder.appendQueryParameter(ITEMS_COUNT, String.valueOf(itemCount));
        }
        int order = filter.getOrder();
        if (order > 0) {
            builder.appendQueryParameter(ORDER, getOrderAsString(order));
        }

        return builder.build().toString();
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
