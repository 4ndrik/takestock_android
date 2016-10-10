package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.local.realmModel.UserRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Victor Artemyev on 09/10/2016.
 */

public class UserRealmDao extends AbstractDao {
    private final RealmConfiguration mRealmConfiguration;

    public UserRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateUser(final User user) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(new UserRealm(user));
            }
        });
        realm.close();
    }

    public User getUserWithId(int id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        UserRealm userRealm = realm
                .where(UserRealm.class)
                .equalTo("mId", id)
                .findFirst();
        return userRealm.getUser();
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(UserRealm.class);
            }
        });
        realm.close();
    }
}
