package com.devabit.takestock.data.model;

import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Category {

    private int mId;
    private String mName;
    private boolean mIsFood;
    private List<Subcategory> mSubcategories;

    public Category() {
    }

    Category(int id, String name, boolean isFood, List<Subcategory> subcategories) {
        mId = id;
        mName = name;
        mIsFood = isFood;
        mSubcategories = subcategories;
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

    public boolean isFood() {
        return mIsFood;
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

    public static class Builder {

        private int mId;
        private String mName;
        private boolean mIsFood;
        private List<Subcategory> mSubcategories;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setIsFood(boolean isFood) {
            mIsFood = isFood;
            return this;
        }

        public Builder setSubcategories(List<Subcategory> subcategories) {
            mSubcategories = subcategories;
            return this;
        }

        public Category build() {
            return new Category(mId, mName, mIsFood, mSubcategories);
        }
    }
}
