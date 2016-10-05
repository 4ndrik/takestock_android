package com.devabit.takestock.screen.shipping;

import android.support.annotation.NonNull;
import android.util.Patterns;
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
 * Created by Victor Artemyev on 05/10/2016.
 */

final class ShippingPresenter implements ShippingContract.Presenter {

    private final DataRepository mDataRepository;
    private final ShippingContract.View mView;

    private CompositeSubscription mSubscriptions;

    ShippingPresenter(@NonNull DataRepository dataRepository, @NonNull ShippingContract.View view) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(ShippingPresenter.this);
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
        return isHouseValid(accept.getHouse())
                && isStreetValid(accept.getStreet())
                && isCityValid(accept.getCity())
                && isPostcodeValid(accept.getCity())
                && isPhoneValid(accept.getPhone());
    }

    private boolean isHouseValid(String house) {
        if (house.isEmpty()) {
            mView.showHouseError();
            return false;
        }
        return true;
    }

    private boolean isStreetValid(String street) {
        if (street.isEmpty()) {
            mView.showStreetError();
            return false;
        }
        return true;
    }

    private boolean isCityValid(String city) {
        if (city.isEmpty()) {
            mView.showCityError();
            return false;
        }
        return true;
    }

    private boolean isPostcodeValid(String postcode) {
        if (postcode.isEmpty()) {
            mView.showPostcodeError();
            return false;
        }
        return true;
    }

    private boolean isPhoneValid(String phone) {
        if (phone.isEmpty()
                || !phone.startsWith("+")
                || !Patterns.PHONE.matcher(phone).matches()) {
            mView.showPhoneError();
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
