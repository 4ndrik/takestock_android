package com.devabit.takestock.widget;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Victor Artemyev on 01/07/2016.
 */
abstract class ListSpacingItemDecoration extends RecyclerView.ItemDecoration {

    protected final int mTop;
    protected final int mBottom;
    protected final int mRight;
    protected final int mLeft;

    public ListSpacingItemDecoration(int space) {
        this(space, space, space, space);
    }

    public ListSpacingItemDecoration(int top, int bottom, int right, int left) {
        mTop = top;
        mBottom = bottom;
        mRight = right;
        mLeft = left;
    }
}
