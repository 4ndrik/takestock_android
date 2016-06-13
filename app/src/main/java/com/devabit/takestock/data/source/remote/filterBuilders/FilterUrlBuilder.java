package com.devabit.takestock.data.source.remote.filterBuilders;

import com.devabit.takestock.data.filters.Filter;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public abstract class FilterUrlBuilder<T extends Filter> {

    protected final StringBuilder mBuilder;
    protected final T mFilter;

    public FilterUrlBuilder(String baseUrl, T filter) {
        mBuilder = new StringBuilder();
        mBuilder.append(baseUrl);
        mFilter = filter;
    }

    public abstract String buildUrl();

    protected void appendQueryParameter(String key, String value) {
        mBuilder.append(mBuilder.charAt(mBuilder.length() - 1) == '/' ? '?' : '&');
        mBuilder.append(key);
        mBuilder.append('=');
        mBuilder.append(value);
    }
}