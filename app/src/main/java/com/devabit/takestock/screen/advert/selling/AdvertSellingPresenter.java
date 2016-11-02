package com.devabit.takestock.screen.advert.selling;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 12/10/2016.
 */

final class AdvertSellingPresenter implements AdvertSellingContract.Presenter {

    private final DataRepository mDataRepository;
    private final AdvertSellingContract.View mView;
    private final int mAdvertId;

    private CompositeSubscription mSubscriptions;

    AdvertSellingPresenter(int advertId, @NonNull DataRepository dataRepository, @NonNull AdvertSellingContract.View view) {
        mAdvertId = advertId;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertSellingPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void loadAdvert() {
        Subscription subscription = mDataRepository.unnotifyAdvertWithId(mAdvertId)
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override public void onNext(Advert advert) {
                        mView.showAdvertInView(advert);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void saveNotification(@NonNull Notification notification) {
        Subscription subscription = mDataRepository.saveNotification(notification)
                .compose(RxTransformers.<Notification>applyObservableSchedulers())
                .subscribe(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        mView.showNotificationSavedInView(notification);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        e(throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        e(throwable);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
