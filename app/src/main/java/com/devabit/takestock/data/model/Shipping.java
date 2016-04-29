package com.devabit.takestock.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Shipping extends RealmObject {

    @PrimaryKey
    private String mType;

    public Shipping(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}
