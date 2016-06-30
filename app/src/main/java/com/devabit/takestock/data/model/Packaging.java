package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class Packaging {

    private int mId;
    private String mType;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override public String toString() {
        return "Packing{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }
}
