package com.devabit.takestock.screen.watching;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.PaginatedList;
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
 * Created by Victor Artemyev on 24/06/2016.
 */
final class WatchingPresenter implements WatchingContract.Presenter {

    private final DataRepository mDataRepository;
    private final WatchingContract.View mWatchingView;

    private CompositeSubscription mSubscriptions;
    private PaginatedList<Advert> mPaginatedList;

    WatchingPresenter(@NonNull DataRepository dataRepository, @NonNull WatchingContract.View watchingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mWatchingView = checkNotNull(watchingView, "watchingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mWatchingView.setPresenter(WatchingPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void refreshAdverts() {
        mWatchingView.setRefreshingProgressIndicator(true);
        Subscription subscription = mDataRepository
                .getPaginatedAdvertListWithFilter(createFilter())
                .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Advert>>() {
                    @Override public void onCompleted() {
                        mWatchingView.setRefreshingProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mWatchingView.setRefreshingProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(PaginatedList<Advert> advertResultList) {
                        mPaginatedList = advertResultList;
                        mWatchingView.showRefreshedAdvertsInView(mPaginatedList.getResults());
                    }
                });
        mSubscriptions.add(subscription);
    }

    private AdvertFilter createFilter() {
        return new AdvertFilter.Builder()
                .setIsWatchlist(true)
                .setOrder(AdvertFilter.ORDER_UPDATED_AT_DESCENDING)
                .create();
    }

    @Override public void loadAdverts() {
        if (mPaginatedList != null && mPaginatedList.hasNext()) {
            mWatchingView.setLoadingProgressIndicator(true);
            Subscription subscription = mDataRepository.getPaginatedAdvertListPerPage(mPaginatedList.getNext())
                    .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                    .subscribe(new Action1<PaginatedList<Advert>>() {
                        @Override public void call(PaginatedList<Advert> paginatedList) {
                            mPaginatedList = paginatedList;
                            mWatchingView.setLoadingProgressIndicator(false);
                            mWatchingView.showLoadedAdvertsInView(mPaginatedList.getResults());
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            mWatchingView.setLoadingProgressIndicator(false);
                            handleError(throwable);
                        }
                    });
            mSubscriptions.add(subscription);
        }
    }

    @Override public void removeWatchingAdvert(final Advert advert) {
        Subscription subscription = mDataRepository.addRemoveAdvertWatching(advert.getId())
                .compose(RxTransformers.<Advert.Subscriber>applyObservableSchedulers())
                .subscribe(new Action1<Advert.Subscriber>() {
                    @Override public void call(Advert.Subscriber subscriber) {
                        if (!subscriber.isSubscribed()) {
                            mWatchingView.showAdvertRemovedFromWatchingInView(advert);
                        } else {
                            mWatchingView.showAdvertRemovedFromWatchingError(advert);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        handleError(throwable);
                        mWatchingView.showAdvertRemovedFromWatchingError(advert);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mWatchingView.showNetworkConnectionError();
        } else {
            mWatchingView.showUnknownError();
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
