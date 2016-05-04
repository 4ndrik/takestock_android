package com.devabit.takestock.data.source.local.entity;

import android.support.annotation.NonNull;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ShippingEntity extends RealmObject {

    @PrimaryKey
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
