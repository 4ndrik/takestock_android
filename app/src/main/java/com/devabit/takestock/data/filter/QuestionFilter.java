package com.devabit.takestock.data.filter;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionFilter extends Filter {

    public static final class Order {
        public static final String UPDATED_AT = "updated_at";
        public static final String UPDATED_AT_DESCENDING = "-updated_at";
        public static final String CREATED_AT = "created_at";
        public static final String CREATED_AT_DESCENDING = "-created_at";
    }

    private final int mAdvertId;
    private final int mUserId;
    private final String mOrder;

    private QuestionFilter(int advertId, int userId, String order) {
        mAdvertId = advertId;
        mUserId = userId;
        mOrder = order;
    }

    public int getAdvertId() {
        return mAdvertId;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getOrder() {
        return mOrder;
    }

    public static final class Builder {

        private int mAdvertId;
        private int mUserId;
        private String mOrder = "";

        public Builder setAdvertId(int advertId) {
            mAdvertId = advertId;
            return this;
        }

        public Builder setUserId(int userId) {
            mUserId = userId;
            return this;
        }

        public Builder setOrder(String order) {
            mOrder = order;
            return this;
        }

        public QuestionFilter create() {
            return new QuestionFilter(mAdvertId, mUserId, mOrder);
        }
    }
}
