package com.devabit.takestock.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockPref;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.screen.advert.selling.AdvertSellingActivity;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.main.MainActivity;
import com.devabit.takestock.screen.advert.buying.AdvertBuyingActivity;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Victor Artemyev on 29/10/2016.
 */

public final class NotificationFactory {

    public static Notification build(@NonNull Context context, @NonNull RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        return new Notification.Builder()
                .setId(getNotificationId(context))
                .setTitle(remoteMessage.getNotification().getTitle())
                .setBody(remoteMessage.getNotification().getBody())
                .setAction(remoteMessage.getNotification().getClickAction())
                .setAdvertId(data.containsKey(Notification.EXTRA_ADVERT_ID)
                        ? Integer.parseInt(data.get(Notification.EXTRA_ADVERT_ID)) : 0)
                .setOfferId(data.containsKey(Notification.EXTRA_OFFER_ID)
                        ? Integer.parseInt(data.get(Notification.EXTRA_OFFER_ID)) : 0)
                .setMessage(data.containsKey(Notification.EXTRA_MESSAGE)
                        ? data.get(Notification.EXTRA_MESSAGE) : "")
                .create();
    }

    public static Notification build(@NonNull Context context, @NonNull Intent intent) {
        return new Notification.Builder()
                .setId(getNotificationId(context))
                .setTitle(intent.getStringExtra(Notification.EXTRA_TITLE))
                .setBody(intent.getStringExtra(Notification.EXTRA_BODY))
                .setAction(intent.getStringExtra(Notification.EXTRA_ACTION))
                .setAdvertId(intent.hasExtra(Notification.EXTRA_ADVERT_ID)
                        ? Integer.parseInt(intent.getStringExtra(Notification.EXTRA_ADVERT_ID)) : 0)
                .setOfferId(intent.hasExtra(Notification.EXTRA_OFFER_ID)
                        ? Integer.parseInt(intent.getStringExtra(Notification.EXTRA_OFFER_ID)) : 0)
                .setMessage(intent.hasExtra(Notification.EXTRA_MESSAGE)
                        ? intent.getStringExtra(Notification.EXTRA_MESSAGE) : "")
                .create();
    }

    private static int getNotificationId(Context context) {
        return TakeStockPref.getNotificationId(context);
    }

    public static void send(@NonNull Context context, @NonNull Notification notification) {
        Intent intent = getStartIntent(context, notification);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder
                = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_tomato_white_24dp)
                .setColor(ContextCompat.getColor(context, R.color.jam))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notification.getId(), notificationBuilder.build());
    }

    public static Intent getStartIntent(Context context, Notification notification) {
        switch (notification.getAction()) {
            case Notification.Action.BUYING:
                return AdvertBuyingActivity.getStartIntent(context, notification);
            case Notification.Action.SELLING:
            case Notification.Action.ADVERT_QUESTION:
                return AdvertSellingActivity.getStartIntent(context, notification);
            case Notification.Action.ADVERT_ANSWER:
                return AdvertDetailActivity.getStartIntent(context, notification.getAdvertId());
            case Notification.Action.MAIN:
            default:
                return MainActivity.getStartIntent(context, notification.getAction());
        }
    }
}
