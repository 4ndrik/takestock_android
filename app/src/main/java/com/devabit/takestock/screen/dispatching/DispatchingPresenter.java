package com.devabit.takestock.screen.dispatching;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 07/10/2016.
 */

final class DispatchingPresenter implements DispatchingContract.Presenter {

    private final DataRepository mDataRepository;
    private final DispatchingContract.View mView;

    private CompositeSubscription mSubscriptions;

    DispatchingPresenter(@NonNull DataRepository dataRepository, @NonNull DispatchingContract.View view) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(DispatchingPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void acceptOffer(final Offer offer, Offer.Accept accept) {
        if (!validateOfferAcceptData(accept)) return;
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.acceptOffer(accept)
                .flatMap(new Func1<Offer.Accept, Observable<Offer>>() {
                    @Override public Observable<Offer> call(Offer.Accept accept) {
                        return mDataRepository.getOfferWithId(accept.getOfferId());
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
                        Timber.d(offer.toString());
                        mView.showOfferAcceptedInView(offer);
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

    private boolean validateOfferAcceptData(Offer.Accept accept) {
        return isArrivalDateValid(accept.getArrivalDate())
                && isPickUpDateValid(accept.getPickUpDate())
                && isTrackingNumberValid(accept.getTracking())
                && isCourierNameValid(accept.getCourierName());
    }

    private boolean isArrivalDateValid(String arrivalDate) {
        if (arrivalDate.isEmpty()) {
            mView.showArrivalDateError();
            return false;
        }
        return true;
    }

    private boolean isPickUpDateValid(String pickUpDate) {
        if (pickUpDate.isEmpty()) {
            mView.showPickUpDateError();
            return false;
        }
        return true;
    }

    private boolean isTrackingNumberValid(String tracking) {
        if (tracking.isEmpty()) {
            mView.showTrackingNumberError();
            return false;
        }
        return true;
    }

    private boolean isCourierNameValid(String courier) {
        if (courier.isEmpty()) {
            mView.showCourierError();
            return false;
        }
        return true;
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
