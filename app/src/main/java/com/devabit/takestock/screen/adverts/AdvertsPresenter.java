package com.devabit.takestock.screen.adverts;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
class AdvertsPresenter implements AdvertsContract.Presenter {

    private DataRepository mDataRepository;
    private AdvertsContract.View mView;
    private AdvertFilter mFilter;
    private CompositeSubscription mSubscriptions;

    private PaginatedList<Advert> mPaginatedList;

    AdvertsPresenter(@NonNull DataRepository dataRepository,
                            @NonNull AdvertsContract.View view,
                            @NonNull AdvertFilter filter) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mFilter = checkNotNull(filter, "filter cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertsPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void loadAdvertsWithFilter(@NonNull AdvertFilter filter) {
        mFilter = filter;
        refreshAdverts();
    }

    @Override public void refreshAdverts() {
        mPaginatedList = null;
        mView.setRefreshingProgressIndicator(true);
        Subscription subscription = mDataRepository
                .getPaginatedAdvertListWithFilter(mFilter)
                .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Advert>>() {
                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setRefreshingProgressIndicator(false);
                        e(throwable);
                        if (throwable instanceof NetworkConnectionException) {
                            mView.showNetworkConnectionError();
                        } else {
                            mView.showUnknownError();
                        }
                    }

                    @Override public void onNext(PaginatedList<Advert> paginatedList) {
                        mView.setRefreshingProgressIndicator(false);
                        mPaginatedList = paginatedList;
                        mView.showTotalAdvertsCountInView(paginatedList.getCount());
                        mView.showRefreshedAdvertsInView(paginatedList.getResults());
                    }
                });
        mSubscriptions.add(subscription);

    }

    @Override public void loadAdverts() {
        if (mPaginatedList != null && mPaginatedList.hasNext()) {
            mView.setLoadingProgressIndicator(true);
            Subscription subscription = mDataRepository
                    .getPaginatedAdvertListPerPage(mPaginatedList.getNext())
                    .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                    .subscribe(new Subscriber<PaginatedList<Advert>>() {
                        @Override public void onCompleted() {
                        }

                        @Override public void onError(Throwable throwable) {
                            mView.setLoadingProgressIndicator(false);
                            e(throwable);
                            if (throwable instanceof NetworkConnectionException) {
                                mView.showNetworkConnectionError();
                            } else {
                                mView.showUnknownError();
                            }
                        }

                        @Override public void onNext(PaginatedList<Advert> paginatedList) {
                            mView.setLoadingProgressIndicator(false);
                            mPaginatedList = paginatedList;
                            mView.showLoadedAdvertsInView(paginatedList.getResults());
                        }
                    });
            mSubscriptions.add(subscription);
        }
    }

    @Override public void addOrRemoveWatchingAdvert(final int advertId) {
        Subscription subscription = mDataRepository.addRemoveAdvertWatching(advertId)
                .compose(RxTransformers.<Advert.Subscriber>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert.Subscriber>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable throwable) {
                        e(throwable);
                        mView.showAdvertWatchingError(advertId);
                    }

                    @Override public void onNext(Advert.Subscriber subscriber) {
                        e(subscriber.toString());
                        if (subscriber.isSubscribed()) {
                            mView.showAdvertAddedToWatching(subscriber.getAdvertId());
                        } else {
                            mView.showAdvertRemovedFromWatching(subscriber.getAdvertId());
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
