package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PackagingEntity extends RealmObject {

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
        return "PackingEntity{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }
}
