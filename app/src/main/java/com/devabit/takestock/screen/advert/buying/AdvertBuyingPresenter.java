package com.devabit.takestock.screen.advert.buying;

import android.support.annotation.NonNull;
import android.util.Pair;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

final class AdvertBuyingPresenter implements AdvertBuyingContract.Presenter {

    private final DataRepository mDataRepository;
    private final AdvertBuyingContract.View mView;

    private CompositeSubscription mSubscriptions;

    AdvertBuyingPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertBuyingContract.View view) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertBuyingPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void acceptOffer(final Offer offer, Offer.Accept accept) {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.acceptOffer(accept)
                .flatMap(new Func1<Offer.Accept, Observable<Offer>>() {
                    @Override public Observable<Offer> call(Offer.Accept accept) {
                        return mDataRepository.getOfferWithId(accept.getOfferId());
                    }
                })
                .map(new Func1<Offer, Offer>() {
                    @Override public Offer call(Offer offer) {
                        Offer currentOffer = offer;
                        while (currentOffer.hasChildOffers()) {
                            currentOffer = currentOffer.getChildOffers()[0];
                        }
                        return currentOffer;
                    }
                })
                .compose(RxTransformers.<Offer>applyObservableSchedulers())
                .subscribe(new Subscriber<Offer>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(Offer offer) {
                        d(offer.toString());
                        mView.showOfferAcceptedInView(offer);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void readNotification(@NonNull Notification notification) {
        Subscription subscription = mDataRepository.readNotification(notification)
                .compose(RxTransformers.<Notification>applyObservableSchedulers())
                .subscribe(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        d("%s - read", notification);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        handleError(throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void loadOfferAdvertPair(int offerId) {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getOfferWithId(offerId)
                .flatMap(new Func1<Offer, Observable<Pair<Offer, Advert>>>() {
                    @Override public Observable<Pair<Offer, Advert>> call(Offer offer) {
                        return Observable.zip(
                                Observable.just(offer),
                                mDataRepository.getAdvertWithId(offer.getAdvertId()),
                                new Func2<Offer, Advert, Pair<Offer, Advert>>() {
                                    @Override public Pair<Offer, Advert> call(Offer offer, Advert advert) {
                                        return Pair.create(offer, advert);
                                    }
                                });
                    }
                })
                .compose(RxTransformers.<Pair<Offer, Advert>>applyObservableSchedulers())
                .subscribe(new Subscriber<Pair<Offer, Advert>>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(Pair<Offer, Advert> offerAdvertPair) {
                        mView.showOfferAdvertPairInView(offerAdvertPair);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void saveNotification(Notification notification) {
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
        if (throwable instanceof NetworkConnectionException) {
            mView.showNetworkConnectionError();
        } else {
            mView.showUnknownError();
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
