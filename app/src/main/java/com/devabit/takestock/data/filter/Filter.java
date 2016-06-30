package com.devabit.takestock.data.filter;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public abstract class Filter {

    private int mPageSize;

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }
}
