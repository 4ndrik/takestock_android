package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Condition {

    private int mId;
    private String mState;

    public Condition(){}

    public Condition(int id, String state) {
        mId = id;
        mState = state;
    }

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
