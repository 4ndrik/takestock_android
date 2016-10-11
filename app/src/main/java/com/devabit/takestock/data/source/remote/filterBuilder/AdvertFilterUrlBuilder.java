package com.devabit.takestock.data.source.remote.filterBuilder;

import android.text.TextUtils;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;

import static com.devabit.takestock.data.filter.AdvertFilter.*;

/**
 * Created by Victor Artemyev on 20/05/2016.
 */
public class AdvertFilterUrlBuilder extends FilterUrlBuilder<AdvertFilter> {

    private static final String ITEMS_COUNT = "items_count";
    private static final String CATEGORY = "category";
    private static final String SUBCATEGORY = "subcategory";
    private static final String AUTHOR_ID = "author_id";
    private static final String ORDER = "o";
    private static final String IDS = "ids";
    private static final String QUERY = "q";
    private static final String IN = "in";

    private static final String EXPIRES_AT = "expires_at";
    private static final String EXPIRES_AT_DESCENDING = "-expires_at";
    private static final String CREATED_AT = "created_at";
    private static final String CREATED_AT_DESCENDING = "-created_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String UPDATED_AT_DESCENDING = "-updated_at";
    private static final String GUIDE_PRICE = "guide_price";
    private static final String GUIDE_PRICE_DESCENDING = "-guide_price";
    private static final String FILTER = "filter";
    private static final String RELATED_BY = "related_by_advert";

    public AdvertFilterUrlBuilder(String baseUrl, AdvertFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        int pageSize = mFilter.getPageSize();
        if (pageSize > 0) {
            appendQueryParameter(PAGE_SIZE, pageSize);
        }

        Category category = mFilter.getCategory();
        if (category != null && category.getId() > 0) {
            appendQueryParameter(CATEGORY, category.getId());
        }

        Subcategory subcategory = mFilter.getSubcategory();
        if (subcategory != null) {
            appendQueryParameter(SUBCATEGORY, subcategory.getId());
        }

        String query = mFilter.getQuery();
        if (!TextUtils.isEmpty(query)) {
            appendQueryParameter(QUERY, query);
        }

        int itemCount = mFilter.getItemCount();
        if (itemCount > 0) {
            appendQueryParameter(ITEMS_COUNT, itemCount);
        }

        int order = mFilter.getOrder();
        if (order > 0) {
            appendQueryParameter(ORDER, getOrderAsString(order));
        }

        int userId = mFilter.getAuthorId();
        if (userId > 0) {
            appendQueryParameter(AUTHOR_ID, userId);
        }

        int[] ids = mFilter.getAdvertIds();
        if (ids != null && ids.length > 0) {
            appendQueryParameter(IDS, ids);
        }

        if (mFilter.hasAdditionalFilter()) {
            appendQueryParameter(FILTER, mFilter.getAdditionalFilter());
        }

        String[] additions = mFilter.getAdditions();
        if (additions != null && additions.length > 0) {
            appendQueryParameter(IN, additions);
        }

        if (mFilter.getRelatedId() > 0) {
            appendQueryParameter(RELATED_BY, mFilter.getRelatedId());
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

            case ORDER_UPDATED_AT:
                return UPDATED_AT;

            case ORDER_UPDATED_AT_DESCENDING:
                return UPDATED_AT_DESCENDING;

            default:
                return "";
        }
    }
}
