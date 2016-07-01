package com.devabit.takestock.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Victor Artemyev on 01/07/2016.
 */
public class ListSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public ListSpacingItemDecoration(int space) {
        mSpace = space;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mSpace;
        outRect.left = mSpace;
        outRect.right = mSpace;
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.top = mSpace;
        }
    }
}
