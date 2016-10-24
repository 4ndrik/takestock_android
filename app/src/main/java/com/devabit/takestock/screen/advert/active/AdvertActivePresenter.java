package com.devabit.takestock.screen.advert.active;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 12/10/2016.
 */

final class AdvertActivePresenter implements AdvertActiveContract.Presenter {

    private final DataRepository mDataRepository;
    private final AdvertActiveContract.View mView;
    private final int mAdvertId;

    private CompositeSubscription mSubscriptions;

    AdvertActivePresenter(int advertId, @NonNull DataRepository dataRepository, @NonNull AdvertActiveContract.View view) {
        mAdvertId = advertId;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertActivePresenter.this);
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

    @Override public void readNotification(@NonNull Notification notification) {
        Subscription subscription = mDataRepository.readNotification(notification)
                .compose(RxTransformers.<Notification>applyObservableSchedulers())
                .subscribe(new Subscriber<Notification>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override public void onNext(Notification notification) {
                        Timber.d("%s - read", notification);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
