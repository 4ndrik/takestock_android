package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Size;
import com.devabit.takestock.data.source.local.realmModel.SizeRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class SizeRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public SizeRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateSizeList(final List<Size> sizeList) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(sizeList));
            }
        });
        realm.close();
    }

    private List<SizeRealm> fromModelToRealm(List<Size> sizeList) {
        List<SizeRealm> result = new ArrayList<>(sizeList.size());
        for (Size size : sizeList) {
            result.add(new SizeRealm(size));
        }
        return result;
    }

    public List<Size> getSizeList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<SizeRealm> results = realm.where(SizeRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<Size> fromRealmToModel(List<SizeRealm> results) {
        List<Size> sizeList = new ArrayList<>(results.size());
        for (SizeRealm sizeRealm : results) {
            sizeList.add(sizeRealm.getSize());
        }
        return sizeList;
    }

    @Override public void clearDatabase() {

    }
}
