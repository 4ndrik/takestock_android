package com.devabit.takestock.data.source.remote.filterBuilder;

import com.devabit.takestock.data.filter.QuestionFilter;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionFilterUrlBuilder extends FilterUrlBuilder<QuestionFilter> {

    private static final String ADVERT_ID = "advert_id";

    public QuestionFilterUrlBuilder(String baseUrl, QuestionFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        int advertId = mFilter.getAdvertId();
        if (advertId > 0) appendQueryParameter(ADVERT_ID, String.valueOf(advertId));
        return mBuilder.toString();
    }
}
