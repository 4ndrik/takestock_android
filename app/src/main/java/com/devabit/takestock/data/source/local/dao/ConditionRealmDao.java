package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.source.local.realmModel.ConditionRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class ConditionRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public ConditionRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateConditionList(final List<Condition> conditionList) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(conditionList));
            }
        });
        realm.close();
    }

    private List<ConditionRealm> fromModelToRealm(List<Condition> conditionList) {
        List<ConditionRealm> result = new ArrayList<>(conditionList.size());
        for (Condition condition : conditionList) {
            result.add(new ConditionRealm(condition));
        }
        return result;
    }

    public List<Condition> getConditionList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<ConditionRealm> results = realm.where(ConditionRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<Condition> fromRealmToModel(List<ConditionRealm> results) {
        List<Condition> conditionList = new ArrayList<>(results.size());
        for (ConditionRealm conditionRealm : results) {
            conditionList.add(conditionRealm.getCondition());
        }
        return conditionList;
    }

    public Condition getConditionWithId(int id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        ConditionRealm shippingRealm = realm
                .where(ConditionRealm.class)
                .equalTo("mId", id)
                .findFirst();
        return shippingRealm.getCondition();
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(ConditionRealm.class);
            }
        });
        realm.close();
    }
}
