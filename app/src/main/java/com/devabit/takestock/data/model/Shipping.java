package com.devabit.takestock.data.model;

import android.support.annotation.NonNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Shipping {

    private int mId;
    private String mType;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setType(@NonNull String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    @Override public String toString() {
        return "Shipping{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }
}
