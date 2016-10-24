package com.devabit.takestock.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockPref;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.screen.advert.active.AdvertActiveActivity;
import com.devabit.takestock.screen.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import rx.Subscriber;
import timber.log.Timber;

import java.util.Map;

import static timber.log.Timber.d;

/**
 * Created by Victor Artemyev on 17/10/2016.
 */

public class TakeStockFirebaseMessagingService extends FirebaseMessagingService {

    private DataRepository mDataRepository;

    @Override public void onCreate() {
        super.onCreate();
        mDataRepository = Injection.provideDataRepository(TakeStockFirebaseMessagingService.this);
    }

    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        d("From: %s", remoteMessage.getFrom());

        Timber.d("Message Type: [ %s ]", remoteMessage.getMessageType());
        Timber.d("Message Sent Time: [ %s ]", remoteMessage.getSentTime());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Timber.d("Message Data: [ %s ]", remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Timber.d("Message Title: [ %s ]", remoteMessage.getNotification().getTitle());
            Timber.d("Message Body: [ %s ]", remoteMessage.getNotification().getBody());
        }

        saveNotification(buildNotification(remoteMessage));

    }

    private Notification buildNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        return new Notification.Builder()
                .setId(getNotificationId())
                .setSentTime(remoteMessage.getSentTime())
                .setTitle(remoteMessage.getNotification().getTitle())
                .setBody(remoteMessage.getNotification().getBody())
                .setType(data.get("type"))
                .setAdvertId(data.containsKey("advert_id") ? Integer.parseInt(data.get("advert_id")) : 0)
                .setOfferId(data.containsKey("offer_id") ? Integer.parseInt(data.get("offer_id")) : 0)
                .setNew(true)
                .create();
    }

    private int getNotificationId() {
        return TakeStockPref.getNotificationId(TakeStockFirebaseMessagingService.this);
    }

    private void saveNotification(Notification notification) {
        mDataRepository.saveNotification(notification)
                .compose(RxTransformers.<Notification>applyObservableSchedulers())
                .subscribe(new Subscriber<Notification>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override public void onNext(Notification notification) {
                        sendNotification(notification);
                    }
                });
    }

    private void sendNotification(Notification notification) {
        Intent intent = getStartIntent(notification);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(TakeStockFirebaseMessagingService.this,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_tomato_white_24dp)
                .setColor(ContextCompat.getColor(TakeStockFirebaseMessagingService.this, R.color.jam))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notification.getId(), notificationBuilder.build());
    }

    private Intent getStartIntent(Notification notification) {
        if (TextUtils.isEmpty(notification.getType())) return MainActivity.getStartIntent(TakeStockFirebaseMessagingService.this, "");
        switch (notification.getType()) {
            default:
                return AdvertActiveActivity.getStartIntent(TakeStockFirebaseMessagingService.this, notification);
        }
    }
}
