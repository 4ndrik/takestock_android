package com.devabit.takestock.screen.watching;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.AdvertSubscriber;
import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 24/06/2016.
 */
final class WatchingPresenter implements WatchingContract.Presenter {

    private static final String TAG = makeLogTag(WatchingPresenter.class);

    private final DataRepository mDataRepository;

    private final WatchingContract.View mWatchingView;

    private CompositeSubscription mSubscriptions;

    WatchingPresenter(@NonNull DataRepository dataRepository, @NonNull WatchingContract.View watchingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mWatchingView = checkNotNull(watchingView, "watchingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mWatchingView.setPresenter(WatchingPresenter.this);
    }

    @Override public void resume() {
        fetchAdverts();
    }

    @Override public void fetchAdverts() {
        mWatchingView.setProgressIndicator(true);
        AdvertFilter filter = new AdvertFilter();
        filter.setPageSize(Integer.MAX_VALUE);
        filter.setWatchlist(true);
        Subscription subscription = mDataRepository
                .getPaginatedAdvertListWithFilter(filter)
                .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Advert>>() {
                    @Override public void onCompleted() {
                        mWatchingView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mWatchingView.setProgressIndicator(false);
                        LOGE(TAG, "BOOM:", e);
                        if (e instanceof NetworkConnectionException) {
                            mWatchingView.showNetworkConnectionError();
                        } else {
                            mWatchingView.showUnknownError();
                        }
                    }

                    @Override public void onNext(PaginatedList<Advert> advertResultList) {
                        List<Advert> adverts = advertResultList.getResults();
                        mWatchingView.showAdvertsInView(adverts);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void removeWatchingAdvert(final int advertId) {
        AdvertSubscriber subscriber = new AdvertSubscriber();
        subscriber.setAdvertId(advertId);
        Subscription subscription = mDataRepository.addRemoveAdvertWatching(subscriber)
                .compose(RxTransformers.<AdvertSubscriber>applyObservableSchedulers())
                .subscribe(new Action1<AdvertSubscriber>() {
                    @Override public void call(AdvertSubscriber subscriber) {
                        LOGD(TAG, subscriber);
                        if (!subscriber.isSubscribed()) {
                            mWatchingView.showAdvertRemovedFromWatchingInView(subscriber.getAdvertId());
                        } else {
                            mWatchingView.showAdvertRemovedFromWatchingError(subscriber.getAdvertId());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                        mWatchingView.showAdvertRemovedFromWatchingError(advertId);
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
