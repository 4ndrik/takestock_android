package com.devabit.takestock.data.model;

import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Category {

    private int mId;
    private String mName;
    private List<Subcategory> mSubcategories;

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

    public List<Subcategory> getSubcategories() {
        return mSubcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        mSubcategories = subcategories;
    }

    @Override public String toString() {
        return "Category{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mSubcategories=" + mSubcategories +
                '}';
    }
}