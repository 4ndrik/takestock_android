package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.OfferStatus;
import com.devabit.takestock.data.source.local.realmModel.OfferStatusRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class OfferStatusRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public OfferStatusRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateOfferStatusList(final List<OfferStatus> offerStatuses) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(offerStatuses));
            }
        });
        realm.close();
    }

    private List<OfferStatusRealm> fromModelToRealm(List<OfferStatus> offerStatuses) {
        List<OfferStatusRealm> result = new ArrayList<>(offerStatuses.size());
        for (OfferStatus offerStatus : offerStatuses) {
            result.add(new OfferStatusRealm(offerStatus));
        }
        return result;
    }

    public List<OfferStatus> getOfferStatusList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<OfferStatusRealm> results = realm.where(OfferStatusRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<OfferStatus> fromRealmToModel(List<OfferStatusRealm> results) {
        List<OfferStatus> offerStatusList = new ArrayList<>(results.size());
        for (OfferStatusRealm offerStatusRealm : results) {
            offerStatusList.add(offerStatusRealm.getOfferStatus());
        }
        return offerStatusList;
    }

    public OfferStatus getOfferStatusWithId(int id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        OfferStatusRealm packagingRealm = realm
                .where(OfferStatusRealm.class)
                .equalTo("mId", id)
                .findFirst();
        return packagingRealm.getOfferStatus();
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(OfferStatusRealm.class);
            }
        });
        realm.close();
    }
}
