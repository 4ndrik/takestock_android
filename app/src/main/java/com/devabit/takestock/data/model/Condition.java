package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Condition {

    private String mState;

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    @Override public String toString() {
        return "Condition{" +
                "mState='" + mState + '\'' +
                '}';
    }
}
