package com.devabit.takestock.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Victor Artemyev on 30/06/2016.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;
    private final int mColumns;

    public GridSpacingItemDecoration(int space, int columns) {
        mSpace = space;
        mColumns = columns;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = 2 * mSpace;
        outRect.left = mSpace;
        outRect.right = mSpace;
        int position = parent.getChildAdapterPosition(view);
        if (position < mColumns) {
            outRect.top = 2 * mSpace;
        }
    }
}
