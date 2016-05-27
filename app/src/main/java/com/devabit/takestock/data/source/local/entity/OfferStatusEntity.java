package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferStatusEntity extends RealmObject {

    @PrimaryKey
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
        return "OfferStatusEntity{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }
}
