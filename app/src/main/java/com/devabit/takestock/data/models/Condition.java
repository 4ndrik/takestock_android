package com.devabit.takestock.data.models;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Condition {

    private int mId;
    private String mState;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    @Override public String toString() {
        return "Condition{" +
                "mId=" + mId +
                ", mState='" + mState + '\'' +
                '}';
    }
}
