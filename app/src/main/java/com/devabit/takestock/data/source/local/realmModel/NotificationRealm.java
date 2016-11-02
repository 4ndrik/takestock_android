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
    private String mMessage;
    private long mSentTime;
    private boolean mIsSaved;

    public NotificationRealm() {
    }

    public NotificationRealm(Notification notification) {
        mId = notification.getId();
        mType = notification.getAction();
        mTitle = notification.getTitle();
        mBody = notification.getBody();
        mOfferId = notification.getOfferId();
        mAdvertId = notification.getAdvertId();
        mMessage = notification.getMessage();
        mSentTime = notification.getSentTime();
        mIsSaved = notification.isSaved();
    }

    public Notification toNotification() {
        return new Notification.Builder()
                .setId(mId)
                .setAction(mType)
                .setTitle(mTitle)
                .setBody(mBody)
                .setOfferId(mOfferId)
                .setAdvertId(mAdvertId)
                .setMessage(mMessage)
                .setSentTime(mSentTime)
                .setSaved(mIsSaved)
                .create();
    }
}
