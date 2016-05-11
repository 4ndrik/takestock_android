package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ShippingEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mType;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    @Override public String toString() {
        return "ShippingEntity{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }
}
