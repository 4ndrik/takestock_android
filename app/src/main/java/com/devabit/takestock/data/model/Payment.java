package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 05/07/2016.
 */
public class Payment {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    private final int mOfferId;
    private final String mTokenId;

    private String mStatus;

    public Payment(int offerId, String tokenId) {
        mOfferId = offerId;
        mTokenId = tokenId;
    }

    public int getOfferId() {
        return mOfferId;
    }

    public String getTokenId() {
        return mTokenId;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public boolean isSuccessful() {
        return mStatus != null && mStatus.equals(SUCCESS);
    }

    @Override public String toString() {
        return "Payment{" +
                "mOfferId=" + mOfferId +
                ", mTokenId='" + mTokenId + '\'' +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}
