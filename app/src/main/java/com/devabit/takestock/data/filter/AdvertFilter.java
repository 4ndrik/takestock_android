package com.devabit.takestock.data.filter;

import android.support.annotation.IntDef;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Victor Artemyev on 20/05/2016.
 */
public class AdvertFilter extends Filter {

    @IntDef({
            ORDER_DEFAULT, ORDER_EXPIRES_AT, ORDER_EXPIRES_AT_DESCENDING, ORDER_CREATED_AT,
            ORDER_CREATED_AT_DESCENDING, ORDER_GUIDE_PRICE, ORDER_GUIDE_PRICE_DESCENDING})
    public @interface Order {}

    public static final int ORDER_DEFAULT = -1;
    public static final int ORDER_EXPIRES_AT = 1;
    public static final int ORDER_EXPIRES_AT_DESCENDING = 2;
    public static final int ORDER_CREATED_AT = 3;
    public static final int ORDER_CREATED_AT_DESCENDING = 4;
    public static final int ORDER_GUIDE_PRICE = 5;
    public static final int ORDER_GUIDE_PRICE_DESCENDING = 6;

    private Set<Integer> mAdvertIds = new HashSet<>(0);
    private int mItemCount;
    private Category mCategory;
    private Subcategory mSubcategory;
    private int mAuthorId;
    private boolean mIsWatchlist;
    private String mQuery;
    @Order private int mOrder;

    public AdvertFilter() {
    }

    AdvertFilter(int itemCount, Category category, Subcategory subcategory, int authorId,
                 int order, int pageSize, boolean isWatchlist, String query) {
        mItemCount = itemCount;
        mCategory = category;
        mSubcategory = subcategory;
        mAuthorId = authorId;
        mOrder = order;
        mPageSize = pageSize;
        mIsWatchlist = isWatchlist;
        mQuery = query;
    }

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

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    public Subcategory getSubcategory() {
        return mSubcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        mSubcategory = subcategory;
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

    public String getQuery() {
        return mQuery;
    }

    public static class Builder {

        private int mItemCount;
        private Category mCategory;
        private Subcategory mSubcategory;
        private int mAuthorId;
        private int mOrder;
        private int mPageSize = DEFAULT_PAGE_SIZE;
        private boolean mIsWatchlist;
        private String mQuery = "";

        public Builder setItemCount(int itemCount) {
            mItemCount = itemCount;
            return this;
        }

        public Builder setCategory(Category category) {
            mCategory = category;
            return this;
        }

        public Builder setSubcategory(Subcategory subcategory) {
            mSubcategory = subcategory;
            return this;
        }

        public Builder setAuthorId(int authorId) {
            mAuthorId = authorId;
            return this;
        }

        public Builder setOrder(@Order int order) {
            mOrder = order;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            mPageSize = pageSize;
            return this;
        }

        public Builder setIsWatchlist(boolean isWatchlist) {
            mIsWatchlist = isWatchlist;
            return this;
        }

        public Builder setQuery(String query) {
            mQuery = query;
            return this;
        }

        public AdvertFilter create() {
            return new AdvertFilter(
                    mItemCount, mCategory, mSubcategory,
                    mAuthorId, mOrder, mPageSize, mIsWatchlist, mQuery);
        }
    }
}
