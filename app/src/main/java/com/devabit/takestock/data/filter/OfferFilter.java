package com.devabit.takestock.data.filter;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferFilter extends Filter {

    public static final class Addition {
        public static final String FROM_SELLER = "from_seller";
        public static final String FROM_BUYER = "from_buyer";
        public static final String ORIGINAL = "original";
    }

    public static final class View {
        public static final String CHILD_OFFERS = "child_offers";
        public static final String LAST_OFFER = "last_offer";
    }

    public static final class Order {
        public static final String CREATED_AT = "created_at";
        public static final String CREATED_AT_DESCENDING = "-created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String UPDATED_AT_DESCENDING = "-updated_at";
    }

    private int mUserId;
    private int mAdvertId;
    private boolean mForSelf;
    private String[] mAdditions;
    private String[] mViews;
    private String mOrder;

    private OfferFilter(){}

    private OfferFilter(int userId, int advertId, boolean forSelf, String[] additions, String[] views, int pageSize, String order) {
        mUserId = userId;
        mAdvertId = advertId;
        mForSelf = forSelf;
        mAdditions = additions;
        mViews = views;
        mPageSize = pageSize;
        mOrder = order;
    }

    public int getAdvertId() {
        return mAdvertId;
    }

    public int getUserId() {
        return mUserId;
    }

    public boolean isForSelf() {
        return mForSelf;
    }

    public String[] getAdditions() {
        return mAdditions;
    }

    public String[] getViews() {
        return mViews;
    }

    public String getOrder() {
        return mOrder;
    }

    public static class Builder {

        private int mUserId;
        private int mAdvertId;
        private boolean mForSelf;
        private String[] mAdditions;
        private String[] mViews;
        private int mPageSize = DEFAULT_PAGE_SIZE;
        private String mOrder;

        public Builder setUserId(int userId) {
            mUserId = userId;
            return this;
        }

        public Builder setAdvertId(int advertId) {
            mAdvertId = advertId;
            return this;
        }

        public Builder setForSelf(boolean forSelf) {
            mForSelf = forSelf;
            return this;
        }

        public Builder setAdditions(String... additions) {
            mAdditions = additions;
            return this;
        }

        public Builder setViews(String... views) {
            mViews = views;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            mPageSize = pageSize;
            return this;
        }

        public Builder setOrder(String order) {
            mOrder = order;
            return this;
        }

        public OfferFilter create() {
            return new OfferFilter(mUserId, mAdvertId, mForSelf, mAdditions, mViews, mPageSize, mOrder);
        }
    }
}
