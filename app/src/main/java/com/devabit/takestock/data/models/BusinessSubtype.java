package com.devabit.takestock.data.models;

/**
 * Created by Victor Artemyev on 22/06/2016.
 */
public class BusinessSubtype {

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
        return "BusinessSubtype{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
}
