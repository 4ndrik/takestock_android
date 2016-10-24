package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Notification;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 20/10/2016.
 */

public class NotificationRealm extends RealmObject {

    @PrimaryKey private int mId;
    private String mType;
    private String mTitle;
    private String mBody;
    private int mOfferId;
    private int mAdvertId;
    private long mSentTime;
    private boolean mIsNew;

    public NotificationRealm() {
    }

    public NotificationRealm(Notification notification) {
        mId = notification.getId();
        mType = notification.getType();
        mTitle = notification.getTitle();
        mBody = notification.getBody();
        mOfferId = notification.getOfferId();
        mAdvertId = notification.getAdvertId();
        mSentTime = notification.getSentTime();
        mIsNew = notification.isNew();
    }

    public Notification toNotification() {
        return new Notification.Builder()
                .setId(mId)
                .setType(mType)
                .setTitle(mTitle)
                .setBody(mBody)
                .setOfferId(mOfferId)
                .setAdvertId(mAdvertId)
                .setSentTime(mSentTime)
                .setNew(mIsNew)
                .create();
    }
}
