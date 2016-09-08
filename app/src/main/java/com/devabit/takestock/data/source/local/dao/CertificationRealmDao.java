package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.source.local.realmModel.CertificationRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class CertificationRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public CertificationRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateCertificationList(final List<Certification> certificationList) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(certificationList));
            }
        });
        realm.close();
    }

    private List<CertificationRealm> fromModelToRealm(List<Certification> certificationList) {
        List<CertificationRealm> result = new ArrayList<>(certificationList.size());
        for (Certification certification : certificationList) {
            result.add(new CertificationRealm(certification));
        }
        return result;
    }

    public List<Certification> getCertificationList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<CertificationRealm> results = realm.where(CertificationRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<Certification> fromRealmToModel(List<CertificationRealm> results) {
        List<Certification> certificationList = new ArrayList<>(results.size());
        for (CertificationRealm certificationRealm : results) {
            certificationList.add(certificationRealm.getCertification());
        }
        return certificationList;
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(CertificationRealm.class);
            }
        });
        realm.close();
    }
}
