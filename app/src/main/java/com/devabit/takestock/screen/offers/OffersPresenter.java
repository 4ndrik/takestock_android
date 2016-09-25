package com.devabit.takestock.screen.offers;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class OffersPresenter implements OffersContract.Presenter {

    private final int mAdvertId;
    private final DataRepository mDataRepository;
    private final OffersContract.View mView;

    private CompositeSubscription mSubscriptions;
    private PaginatedList<Offer> mPaginatedList;

    OffersPresenter(int advertId, @NonNull DataRepository dataRepository, @NonNull OffersContract.View view) {
        mAdvertId = advertId;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(OffersPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void refreshOffers() {
        mView.setRefreshingProgressIndicator(true);
        Subscription subscription = mDataRepository
                .getPaginatedOfferListWithFilter(createFilter())
                .compose(RxTransformers.<PaginatedList<Offer>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Offer>>() {
                    @Override public void onCompleted() {
                        mView.setRefreshingProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setRefreshingProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(PaginatedList<Offer> paginatedList) {
                        mPaginatedList = paginatedList;
                        mView.showRefreshedOffersInView(mPaginatedList.getResults());
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

    private OfferFilter createFilter() {
        return new OfferFilter.Builder()
                .setAdvertId(mAdvertId)
                .create();
    }

    @Override public void loadOffers() {

    }

    @Override public void acceptOffer(final Offer offer, Offer.Accept accept) {
        mView.setRefreshingProgressIndicator(true);
        Subscription subscription = mDataRepository.acceptOffer(accept)
                .compose(RxTransformers.<Offer.Accept>applyObservableSchedulers())
                .subscribe(new Subscriber<Offer.Accept>() {
                    @Override public void onCompleted() {
                        mView.setRefreshingProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mView.setRefreshingProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(Offer.Accept accept) {
                        d(accept.toString());
                        offer.setStatus(accept.getStatus());
                        mView.showOfferAcceptedInView(offer);
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
