package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class SubcategoryEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override public String toString() {
        return "Subcategory{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
}
