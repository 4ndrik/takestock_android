package com.devabit.takestock.data.source.remote.filterBuilder;

import android.text.TextUtils;
import com.devabit.takestock.data.filter.OfferFilter;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public class OfferFilterUrlBuilder extends FilterUrlBuilder<OfferFilter> {

    private static final String USER_ID = "user";
    private static final String ADVERT_ID = "adverts";
    private static final String IN = "in";
    private static final String FOR = "for";
    private static final String VIEW = "view";

    public OfferFilterUrlBuilder(String baseUrl, OfferFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        int pageSize = mFilter.getPageSize();
        if (pageSize > 0) appendQueryParameter(PAGE_SIZE, pageSize);

        int userId = mFilter.getUserId();
        if (userId > 0) appendQueryParameter(USER_ID, String.valueOf(userId));

        int advertId = mFilter.getAdvertId();
        if (advertId > 0) appendQueryParameter(ADVERT_ID, String.valueOf(advertId));

        String[] additions = mFilter.getAdditions();
        if (additions != null && additions.length > 0) {
            appendQueryParameter(IN, TextUtils.join(",", additions));
        }

        String[] views = mFilter.getViews();
        if (views != null && views.length > 0) {
            appendQueryParameter(VIEW, TextUtils.join(",", views));
        }

        if (mFilter.isForSelf()) {
            appendQueryParameter(FOR, "self");
        }

        return mBuilder.toString();
    }
}
