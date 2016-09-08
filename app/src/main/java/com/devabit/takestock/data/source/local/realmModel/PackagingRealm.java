package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Packaging;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PackagingRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mType;

    public PackagingRealm(Packaging packaging) {
        mId = packaging.getId();
        mType = packaging.getType();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Packaging getPackaging() {
        return new Packaging(mId, mType);
    }
}
