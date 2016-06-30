package com.devabit.takestock.data.source.remote.filterBuilder;

import com.devabit.takestock.data.filter.Filter;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public abstract class FilterUrlBuilder<T extends Filter> {

    protected static final String PAGE_SIZE = "page_size";

    protected final StringBuilder mBuilder;
    protected final T mFilter;

    public FilterUrlBuilder(String baseUrl, T filter) {
        mBuilder = new StringBuilder();
        mBuilder.append(baseUrl);
        mFilter = filter;
    }

    public abstract String buildUrl();

    protected void appendQueryParameter(String key, int value) {
        appendKey(key);
        mBuilder.append(value);
    }

    protected void appendQueryParameter(String key, String value) {
        appendKey(key);
        mBuilder.append(value);
    }

    private void appendKey(String key) {
        mBuilder.append(mBuilder.charAt(mBuilder.length() - 1) == '/' ? '?' : '&');
        mBuilder.append(key);
        mBuilder.append('=');
    }
}
