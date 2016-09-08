package com.devabit.takestock.data.model;

import android.support.annotation.NonNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */

public class Size {

    private int mId;
    private String mType;

    public Size(int id, String type) {
        mId = id;
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setType(@NonNull String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    @Override public String toString() {
        return "Size{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }
}
