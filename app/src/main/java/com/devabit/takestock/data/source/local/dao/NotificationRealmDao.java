package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.source.local.realmModel.NotificationRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 20/10/2016.
 */

public class NotificationRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public NotificationRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateNotification(final Notification notification) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(new NotificationRealm(notification));
            }
        });
        realm.close();
    }

    public List<Notification> getNotificationList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<NotificationRealm> results = realm.where(NotificationRealm.class)
                .findAllSorted("mId", Sort.DESCENDING);
        return fromRealmToModel(results);
    }

    private List<Notification> fromRealmToModel(List<NotificationRealm> results) {
        List<Notification> notificationList = new ArrayList<>(results.size());
        for (NotificationRealm notificationRealm : results) {
            notificationList.add(notificationRealm.toNotification());
        }
        return notificationList;
    }

    public long getNewNotificationCount() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        return realm.where(NotificationRealm.class)
                .equalTo("mIsNew", true)
                .count();
    }

    public void removeNotification(final Notification notification) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        final RealmResults<NotificationRealm> results = realm.where(NotificationRealm.class)
                .equalTo("mId", notification.getId())
                .findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(NotificationRealm.class);
            }
        });
        realm.close();
    }
}
