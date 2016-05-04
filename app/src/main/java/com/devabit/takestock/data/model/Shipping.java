package com.devabit.takestock.data.model;

import android.support.annotation.NonNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Shipping {

    private String mType;

    public void setType(@NonNull String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    @Override public String toString() {
        return "Shipping{" +
                "mType='" + mType + '\'' +
                '}';
    }
}
