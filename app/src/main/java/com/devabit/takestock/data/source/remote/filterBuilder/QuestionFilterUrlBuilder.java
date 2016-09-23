package com.devabit.takestock.data.source.remote.filterBuilder;

import com.devabit.takestock.data.filter.QuestionFilter;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionFilterUrlBuilder extends FilterUrlBuilder<QuestionFilter> {

    private static final String ADVERTS = "adverts";
    private static final String USER_ID = "user_id";
    private static final String ORDER = "o";

    public QuestionFilterUrlBuilder(String baseUrl, QuestionFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        int advertId = mFilter.getAdvertId();
        if (advertId > 0) appendQueryParameter(ADVERTS, String.valueOf(advertId));

        int userId = mFilter.getUserId();
        if (userId > 0) appendQueryParameter(USER_ID, userId);

        String order = mFilter.getOrder();
        if (!order.isEmpty()) appendQueryParameter(ORDER, order);

        return mBuilder.toString();
    }
}
