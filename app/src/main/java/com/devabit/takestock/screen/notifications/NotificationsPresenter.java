package com.devabit.takestock.screen.notifications;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class NotificationsPresenter implements NotificationsContract.Presenter {

    private final DataRepository mDataRepository;
    private final NotificationsContract.View mView;
    private final CompositeSubscription mSubscriptions;

    NotificationsPresenter(@NonNull DataRepository dataRepository, @NonNull NotificationsContract.View view) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(NotificationsPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void loadNotifications() {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getNotifications()
                .compose(RxTransformers.<List<Notification>>applyObservableSchedulers())
                .subscribe(new Subscriber<List<Notification>>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        e(e);
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onNext(List<Notification> notifications) {
                        mView.showNotificationsInView(notifications);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void removeNotification(Notification notification) {
        Subscription subscription = mDataRepository.removeNotification(notification)
                .compose(RxTransformers.<Notification>applyObservableSchedulers())
                .subscribe(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        mView.showNotificationRemovedInView(notification);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        e(throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
