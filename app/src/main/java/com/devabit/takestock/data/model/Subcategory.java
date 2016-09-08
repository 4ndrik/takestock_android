package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Subcategory {

    private int mId;
    private String mName;

    public Subcategory() {
    }

    public Subcategory(int id, String name) {
        mId = id;
        mName = name;
    }

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
