package com.devabit.takestock.data.filter;

import android.support.annotation.IntDef;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
            ORDER_DEFAULT, ORDER_EXPIRES_AT, ORDER_EXPIRES_AT_DESCENDING, ORDER_CREATED_AT,
            ORDER_CREATED_AT_DESCENDING, ORDER_GUIDE_PRICE, ORDER_GUIDE_PRICE_DESCENDING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Order {
    }

    public static final class Addition {
        public static final String DRAFTS = "drafts";
        public static final String POSTED = "posted";
        public static final String ACTIVE = "active";
        public static final String HOLD_ON = "hold_on";
    }

    private int mItemCount;
    private Category mCategory;
    private Subcategory mSubcategory;
    private int mAuthorId;
    private boolean mIsWatchlist;
    private String mQuery;
    @Order private int mOrder;
    private String[] mAdditions;
    private int[] mAdvertIds;

    public AdvertFilter() {
    }

    private AdvertFilter(int itemCount, Category category, Subcategory subcategory, int authorId, int order,
                         int pageSize, boolean isWatchlist, String query, String[] additions, int[] advertIds) {
        mItemCount = itemCount;
        mCategory = category;
        mSubcategory = subcategory;
        mAuthorId = authorId;
        mOrder = order;
        mPageSize = pageSize;
        mIsWatchlist = isWatchlist;
        mQuery = query;
        mAdditions = additions;
        mAdvertIds = advertIds;
    }

    public int[] getAdvertIds() {
        return mAdvertIds;
    }

    public int getItemCount() {
        return mItemCount;
    }

    public Category getCategory() {
        return mCategory;
    }

    public Subcategory getSubcategory() {
        return mSubcategory;
    }

    public int getAuthorId() {
        return mAuthorId;
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

    public String[] getAdditions() {
        return mAdditions;
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
        private String[] mAdditions;
        private int[] mAdvertIds;

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

        public Builder setAdditions(String... additions) {
            mAdditions = additions;
            return this;
        }

        public Builder setAdvertIds(int[] advertIds) {
            mAdvertIds = advertIds;
            return this;
        }

        public AdvertFilter create() {
            return new AdvertFilter(mItemCount, mCategory, mSubcategory, mAuthorId,
                    mOrder, mPageSize, mIsWatchlist, mQuery, mAdditions, mAdvertIds);
        }
    }
}
