package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Shipping;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ShippingRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mType;

    public ShippingRealm() {
    }

    public ShippingRealm(Shipping shipping) {
        mId = shipping.getId();
        mType = shipping.getType();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    public Shipping getShipping() {
        return new Shipping(mId, mType);
    }
}
