package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class StringEntity extends RealmObject {

    @PrimaryKey
    private String mValue;

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    @Override public String toString() {
        return mValue;
    }
}
