package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Packaging;
import com.devabit.takestock.data.source.local.realmModel.PackagingRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class PackagingRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public PackagingRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdatePackagingList(final List<Packaging> packagingList) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(packagingList));
            }
        });
        realm.close();
    }

    private List<PackagingRealm> fromModelToRealm(List<Packaging> conditionList) {
        List<PackagingRealm> result = new ArrayList<>(conditionList.size());
        for (Packaging packaging : conditionList) {
            result.add(new PackagingRealm(packaging));
        }
        return result;
    }

    public List<Packaging> getPackagingList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<PackagingRealm> results = realm.where(PackagingRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<Packaging> fromRealmToModel(List<PackagingRealm> results) {
        List<Packaging> conditionList = new ArrayList<>(results.size());
        for (PackagingRealm packagingRealm : results) {
            conditionList.add(packagingRealm.getPackaging());
        }
        return conditionList;
    }

    public Packaging getPackagingWithId(int id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        PackagingRealm packagingRealm = realm
                .where(PackagingRealm.class)
                .equalTo("mId", id)
                .findFirst();
        return packagingRealm.getPackaging();
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(PackagingRealm.class);
            }
        });
        realm.close();
    }
}
