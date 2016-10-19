package com.devabit.takestock.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import timber.log.Timber;

import static timber.log.Timber.d;

/**
 * Created by Victor Artemyev on 17/10/2016.
 */

public class TakeStockFirebaseMessagingService extends FirebaseMessagingService {

    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        d("From: %s", remoteMessage.getFrom());

        Timber.d("Message Type: [ %s ]", remoteMessage.getMessageType());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Timber.d("Message data: [ %s ]", remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Timber.d("Message Title: [ %s ]", remoteMessage.getNotification().getTitle());
            Timber.d("Message Body: [ %s ]", remoteMessage.getNotification().getBody());
        }
    }
}
