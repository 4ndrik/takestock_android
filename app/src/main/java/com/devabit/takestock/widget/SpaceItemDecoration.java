package com.devabit.takestock.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Victor Artemyev on 30/06/2016.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


    private final int mSpace;

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = 2 * mSpace;
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = mSpace;
        outRect.right = mSpace;
        if (pos < 2) outRect.top = 2 * mSpace;
    }
}
