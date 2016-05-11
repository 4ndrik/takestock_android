package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ConditionEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mState;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    @Override public String toString() {
        return "Condition{" +
                "mState='" + mState + '\'' +
                '}';
    }
}
