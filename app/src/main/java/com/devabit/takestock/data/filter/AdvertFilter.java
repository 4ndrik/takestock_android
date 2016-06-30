package com.devabit.takestock.data.filter;

import android.support.annotation.IntDef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Victor Artemyev on 20/05/2016.
 */
public class AdvertFilter extends Filter {

    public static final int ORDER_DEFAULT = -1;
    public static final int ORDER_EXPIRES_AT = 1;
    public static final int ORDER_EXPIRES_AT_DESCENDING = 2;
    public static final int ORDER_CREATED_AT = 3;
    public static final int ORDER_CREATED_AT_DESCENDING = 4;
    public static final int ORDER_GUIDE_PRICE = 5;
    public static final int ORDER_GUIDE_PRICE_DESCENDING = 6;

    @IntDef({
            ORDER_DEFAULT,
            ORDER_EXPIRES_AT, ORDER_EXPIRES_AT_DESCENDING,
            ORDER_CREATED_AT, ORDER_CREATED_AT_DESCENDING,
            ORDER_GUIDE_PRICE, ORDER_GUIDE_PRICE_DESCENDING})
    public @interface Order {}

    private Set<Integer> mAdvertIds = new HashSet<>(0);
    private int mItemCount;
    private int mCategoryId;
    private int mAuthorId;
    @Order private int mOrder;
    private boolean mIsWatchlist;

    public Set<Integer> getAdvertIds() {
        return mAdvertIds;
    }

    public void setAdvertIds(List<Integer> advertIds) {
        mAdvertIds.addAll(advertIds);
    }

    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int authorId) {
        mAuthorId = authorId;
    }

    @Order public int getOrder() {
        return mOrder;
    }

    public void setOrder(@Order int order) {
        mOrder = order;
    }

    public boolean isWatchlist() {
        return mIsWatchlist;
    }

    public void setWatchlist(boolean watchlist) {
        mIsWatchlist = watchlist;
    }
}
