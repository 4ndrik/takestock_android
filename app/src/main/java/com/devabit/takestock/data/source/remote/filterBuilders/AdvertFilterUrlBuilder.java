package com.devabit.takestock.data.source.remote.filterBuilders;

import android.text.TextUtils;
import com.devabit.takestock.data.filters.AdvertFilter;

import java.util.Set;

import static com.devabit.takestock.data.filters.AdvertFilter.*;

/**
 * Created by Victor Artemyev on 20/05/2016.
 */
public class AdvertFilterUrlBuilder extends FilterUrlBuilder<AdvertFilter> {

    private static final String ITEMS_COUNT = "items_count";
    private static final String CATEGORY = "category";
    private static final String AUTHOR_ID = "author_id";
    private static final String ORDER = "o";
    private static final String IDS = "ids";

    public static final String EXPIRES_AT = "expires_at";
    public static final String EXPIRES_AT_DESCENDING = "-expires_at";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_AT_DESCENDING = "-created_at";
    public static final String GUIDE_PRICE = "guide_price";
    public static final String GUIDE_PRICE_DESCENDING = "-guide_price";

    public AdvertFilterUrlBuilder(String baseUrl, AdvertFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        int itemCount = mFilter.getItemCount();
        if (itemCount > 0) {
            appendQueryParameter(ITEMS_COUNT, String.valueOf(itemCount));
        }
        int order = mFilter.getOrder();
        if (order > 0) {
            appendQueryParameter(ORDER, getOrderAsString(order));
        }
        int userId = mFilter.getAuthorId();
        if (userId > 0) {
            appendQueryParameter(AUTHOR_ID, String.valueOf(userId));
        }
        Set<Integer> ids = mFilter.getAdvertIds();
        if (!ids.isEmpty()) {
            appendQueryParameter(IDS, TextUtils.join(",", ids));
        }
        return mBuilder.toString();
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
