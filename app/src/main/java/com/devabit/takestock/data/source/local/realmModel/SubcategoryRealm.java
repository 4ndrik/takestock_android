package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Subcategory;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class SubcategoryRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mName;

    public SubcategoryRealm() {}

    public SubcategoryRealm(Subcategory subcategory) {
        mId = subcategory.getId();
        mName = subcategory.getName();
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

    public Subcategory getSubcategory() {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(mId);
        subcategory.setName(mName);
        return subcategory;
    }

    @Override public String toString() {
        return "SubcategoryRealm{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
}
