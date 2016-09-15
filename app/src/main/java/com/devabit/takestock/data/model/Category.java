package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Category implements Parcelable {

    public static final Category ALL = new Category(0, "All", false, Collections.<Subcategory>emptyList());

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

    protected Category(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIsFood = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeByte((byte) (mIsFood ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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
