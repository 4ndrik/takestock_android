package com.devabit.takestock.data.source.remote.filterBuilder;

import com.devabit.takestock.data.filter.OfferFilter;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public class OfferFilterUrlBuilder extends FilterUrlBuilder<OfferFilter> {

    private static final String USER_ID = "user";
    private static final String ADVERT_ID = "advert";

    public OfferFilterUrlBuilder(String baseUrl, OfferFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        int userId = mFilter.getUserId();
        if (userId > 0) appendQueryParameter(USER_ID, String.valueOf(userId));
        int advertId = mFilter.getAdvertId();
        if (advertId > 0) appendQueryParameter(ADVERT_ID, String.valueOf(advertId));
        return mBuilder.toString();
    }
}
