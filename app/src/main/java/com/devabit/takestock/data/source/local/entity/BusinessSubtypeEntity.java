package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 22/06/2016.
 */
public class BusinessSubtypeEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mName;
    private RealmList<BusinessSubtypeEntity> mSubtypeEntities;

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

    public RealmList<BusinessSubtypeEntity> getSubtypeEntities() {
        return mSubtypeEntities;
    }

    @Override public String toString() {
        return "BusinessSubtypeEntity{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mSubtypeEntities=" + mSubtypeEntities +
                '}';
    }
}
