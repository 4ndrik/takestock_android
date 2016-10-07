package com.devabit.takestock.screen.advert.detail;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class AdvertDetailPresenter implements AdvertDetailContract.Presenter {

    private final DataRepository mDataRepository;
    private final AdvertDetailContract.View mAdvertView;

    private CompositeSubscription mSubscriptions;

    AdvertDetailPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertDetailContract.View advertView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mAdvertView = checkNotNull(advertView, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mAdvertView.setPresenter(AdvertDetailPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void fetchShippingById(int id) {
        Shipping shipping = mDataRepository.getShippingWithId(id);
        if (shipping == null) return;
        mAdvertView.showShippingInView(shipping);
    }

    @Override public void fetchConditionById(int id) {
        Condition condition = mDataRepository.getConditionWithId(id);
        if (condition == null) return;
        mAdvertView.showConditionInView(condition);
    }

    @Override public void makeOffer(Offer offer) {
        mAdvertView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .makeOffer(offer)
                .compose(RxTransformers.<Offer>applyObservableSchedulers())
                .subscribe(new Subscriber<Offer>() {
                    @Override public void onCompleted() {
                        mAdvertView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mAdvertView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(Offer offer) {
                        mAdvertView.showOfferMadeInView(offer);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mAdvertView.showNetworkConnectionError();
        } else {
            mAdvertView.showUnknownError();
        }
    }

    @Override public void addOrRemoveWatchingAdvert(final Advert advert, final int userId) {
        Subscription subscription = mDataRepository.addRemoveAdvertWatching(advert.getId())
                .doOnNext(new Action1<Advert.Subscriber>() {
                    @Override public void call(Advert.Subscriber subscriber) {
                        if (subscriber.isSubscribed()) {
                            advert.addSubscriber(userId);
                        } else {
                            advert.removeSubscriber(userId);
                        }
                    }
                })
                .compose(RxTransformers.<Advert.Subscriber>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert.Subscriber>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable throwable) {
                        handleError(throwable);
                        mAdvertView.showAdvertWatchingError(advert);
                    }

                    @Override public void onNext(Advert.Subscriber subscriber) {
                        Timber.d(subscriber.toString());
                        if (subscriber.isSubscribed()) {
                            mAdvertView.showAdvertAddedToWatching(advert);
                        } else {
                            mAdvertView.showAdvertRemovedFromWatching(advert);
                        }
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
