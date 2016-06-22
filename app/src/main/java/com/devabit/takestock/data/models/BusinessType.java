package com.devabit.takestock.data.models;

import java.util.List;

/**
 * Created by Victor Artemyev on 22/06/2016.
 */
public class BusinessType {

    private int mId;
    private String mName;
    private List<BusinessSubtype> mSubtypes;

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

    public List<BusinessSubtype> getSubtypes() {
        return mSubtypes;
    }

    public void setSubtypes(List<BusinessSubtype> subtypes) {
        mSubtypes = subtypes;
    }

    @Override public String toString() {
        return "BusinessType{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mSubtypes=" + mSubtypes +
                '}';
    }
}
