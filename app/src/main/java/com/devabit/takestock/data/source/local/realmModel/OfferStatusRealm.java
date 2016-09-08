package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.OfferStatus;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferStatusRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mType;

    public OfferStatusRealm() {
    }

    public OfferStatusRealm(OfferStatus offerStatus) {
        mId = offerStatus.getId();
        mType = offerStatus.getType();
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

    public OfferStatus getOfferStatus() {
        return new OfferStatus(mId, mType);
    }
}
