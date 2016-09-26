package com.devabit.takestock.data.filter;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public abstract class Filter {

    static final int DEFAULT_PAGE_SIZE = 25;

    protected int mPageSize;

    public int getPageSize() {
        return mPageSize;
    }
}
