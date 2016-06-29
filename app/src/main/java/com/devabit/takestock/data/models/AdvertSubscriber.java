package com.devabit.takestock.data.models;

import android.support.annotation.StringDef;

/**
 * Created by Victor Artemyev on 29/06/2016.
 */
public class AdvertSubscriber {

    private static final String SUBSCRIBED = "subscribed";
    private static final String UNSUBSCRIBED = "unsubscribed";

    @StringDef({SUBSCRIBED, UNSUBSCRIBED})
    public @interface Status {}

    private int mAdvertId;
    @Status private String mStatus;

    public int getAdvertId() {
        return mAdvertId;
    }

    public void setAdvertId(int advertId) {
        mAdvertId = advertId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public boolean isSubscribed() {
        return SUBSCRIBED.equals(mStatus);
    }

    @Override public String toString() {
        return "AdvertSubscriber{" +
                "mAdvertId=" + mAdvertId +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}
