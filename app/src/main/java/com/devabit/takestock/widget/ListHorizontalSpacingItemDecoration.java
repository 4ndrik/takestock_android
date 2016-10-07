package com.devabit.takestock.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Victor Artemyev on 25/08/2016.
 */
public class ListHorizontalSpacingItemDecoration extends ListSpacingItemDecoration {

    public ListHorizontalSpacingItemDecoration(int space) {
        super(space);
    }

    public ListHorizontalSpacingItemDecoration(int top, int bottom, int right, int left) {
        super(top, bottom, right, left);
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = mTop;
        outRect.bottom = mBottom;
        outRect.left = mLeft;
        outRect.right = mRight;
    }
}
