package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 05/07/2016.
 */
public class Payment {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String BACS_NON_CONFIRMED = "bacs-nonconfirmed";

    public static final class Type {
        public static final String CARD = "card";
        public static final String BACS = "bacs";
    }

    private final int mOfferId;
    private final String mTokenId;
    private final String mType;

    private String mStatus;

    public Payment(int offerId, String tokenId, String type) {
        mOfferId = offerId;
        mTokenId = tokenId;
        mType = type;
    }

    public int getOfferId() {
        return mOfferId;
    }

    public String getTokenId() {
        return mTokenId;
    }

    public String getType() {
        return mType;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public boolean isSuccessful() {
        return mStatus != null && (mStatus.equals(SUCCESS) || mStatus.equals(BACS_NON_CONFIRMED));
    }

    @Override public String toString() {
        return "Payment{" +
                "mOfferId=" + mOfferId +
                ", mTokenId='" + mTokenId + '\'' +
                ", mType='" + mType + '\'' +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}
