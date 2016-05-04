package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class CategoryEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mName;
    private RealmList<SubcategoryEntity> mSubcategories;

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

    public RealmList<SubcategoryEntity> getSubcategories() {
        return mSubcategories;
    }

    @Override public String toString() {
        return "Category{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mSubcategories=" + mSubcategories +
                '}';
    }
}
