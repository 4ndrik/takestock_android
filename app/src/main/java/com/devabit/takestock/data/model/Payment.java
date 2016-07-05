package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 05/07/2016.
 */
public class Payment {

    private int mOfferId;
    private String mTokenId;

    public int getOfferId() {
        return mOfferId;
    }

    public void setOfferId(int offerId) {
        mOfferId = offerId;
    }

    public String getTokenId() {
        return mTokenId;
    }

    public void setTokenId(String tokenId) {
        mTokenId = tokenId;
    }

    @Override public String toString() {
        return "Payment{" +
                "mOfferId=" + mOfferId +
                ", mTokenId='" + mTokenId + '\'' +
                '}';
    }
}
