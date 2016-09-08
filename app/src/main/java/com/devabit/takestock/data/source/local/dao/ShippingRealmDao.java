package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.data.source.local.realmModel.ShippingRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class ShippingRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public ShippingRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateShippingList(final List<Shipping> shippingList) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(shippingList));
            }
        });
        realm.close();
    }

    private List<ShippingRealm> fromModelToRealm(List<Shipping> shippingList) {
        List<ShippingRealm> result = new ArrayList<>(shippingList.size());
        for (Shipping shipping : shippingList) {
            result.add(new ShippingRealm(shipping));
        }
        return result;
    }

    public List<Shipping> getShippingList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<ShippingRealm> results = realm.where(ShippingRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<Shipping> fromRealmToModel(List<ShippingRealm> results) {
        List<Shipping> shippingList = new ArrayList<>(results.size());
        for (ShippingRealm shippingRealm : results) {
            shippingList.add(shippingRealm.getShipping());
        }
        return shippingList;
    }

    public Shipping getShippingWithId(int id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        ShippingRealm shippingRealm = realm
                .where(ShippingRealm.class)
                .equalTo("mId", id)
                .findFirst();
        return shippingRealm.getShipping();
    }

    @Override public void clearDatabase() {

    }
}
