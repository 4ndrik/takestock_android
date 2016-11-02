package com.devabit.takestock.fcm;

import com.devabit.takestock.Injection;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.devabit.takestock.utils.NotificationFactory.build;
import static com.devabit.takestock.utils.NotificationFactory.send;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

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
        d("Message Type: [ %s ]", remoteMessage.getMessageType());
        d("Message Sent Time: [ %s ]", remoteMessage.getSentTime());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            d("Message Data: [ %s ]", remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            d("Message Title: [ %s ]", remoteMessage.getNotification().getTitle());
            d("Message Body: [ %s ]", remoteMessage.getNotification().getBody());
        }

        Notification notification = build(TakeStockFirebaseMessagingService.this, remoteMessage);
        saveNotification(notification);
    }

    private void saveNotification(Notification notification) {
        if (notification.getAction() == null) {
            return;
        }
        buildSaveNotificationObservable(notification)
                .compose(RxTransformers.<Notification>applyObservableSchedulers())
                .subscribe(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        send(TakeStockFirebaseMessagingService.this, notification);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        e(throwable);
                    }
                });
    }

    private Observable<Notification> buildSaveNotificationObservable(final Notification notification) {
        if (notification.getAction().equals(Notification.Action.MAIN)) {
            return mDataRepository.refreshAccountUserWithId(getUserId())
                    .flatMap(new Func1<User, Observable<Notification>>() {
                        @Override public Observable<Notification> call(User user) {
                            d(user.toString());
                            return mDataRepository.saveNotification(notification);
                        }
                    });
        }
        return mDataRepository.saveNotification(notification);
    }

    private int getUserId() {
        return TakeStockAccount.get(TakeStockFirebaseMessagingService.this).getId();
    }
}
