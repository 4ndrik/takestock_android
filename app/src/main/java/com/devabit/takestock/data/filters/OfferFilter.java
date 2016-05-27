package com.devabit.takestock.data.filters;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferFilter implements Filter {

    private int mUserId;
    private int mAdvertId;

    public int getAdvertId() {
        return mAdvertId;
    }

    public void setAdvertId(int advertId) {
        mAdvertId = advertId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
