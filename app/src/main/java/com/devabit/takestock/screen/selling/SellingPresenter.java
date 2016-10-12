package com.devabit.takestock.screen.selling;

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
 * Created by Victor Artemyev on 29/04/2016.
 */
public abstract class SellingPresenter implements SellingContract.Presenter {

    protected final int mUserId;
    private DataRepository mDataRepository;
    private SellingContract.View mSellingView;

    private CompositeSubscription mSubscriptions;
    private PaginatedList<Advert> mAdvertPaginatedList;

    protected SellingPresenter(int userId, @NonNull DataRepository dataRepository, @NonNull SellingContract.View sellingView) {
        mUserId = userId;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSellingView = checkNotNull(sellingView, "sellingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSellingView.setPresenter(SellingPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void refreshAdverts() {
        mSellingView.setRefreshingProgressIndicator(true);
        Subscription subscription = mDataRepository
                .getPaginatedAdvertListWithFilter(createFilter())
                .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Advert>>() {
                    @Override public void onCompleted() {
                        mSellingView.setRefreshingProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mSellingView.setRefreshingProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(PaginatedList<Advert> paginatedList) {
                        mAdvertPaginatedList = paginatedList;
                        mSellingView.showRefreshedAdvertsInView(paginatedList.getResults());
                    }
                });
        mSubscriptions.add(subscription);
    }

//    private AdvertFilter createFilter() {
//        return new AdvertFilter.Builder()
//                .setAuthorId(mUserId)
//                .setOrder(AdvertFilter.ORDER_UPDATED_AT_DESCENDING)
//                .setAdditions(AdvertFilter.Addition.POSTED, AdvertFilter.Addition.HOLD_ON)
//                .create();
//    }
    protected abstract AdvertFilter createFilter();

    @Override public void loadAdverts() {
        if (mAdvertPaginatedList != null && mAdvertPaginatedList.hasNext()) {
            mSellingView.setLoadingProgressIndicator(true);
            Subscription subscription = mDataRepository
                    .getPaginatedAdvertListPerPage(mAdvertPaginatedList.getNext())
                    .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                    .subscribe(new Subscriber<PaginatedList<Advert>>() {
                        @Override public void onCompleted() {
                            mSellingView.setLoadingProgressIndicator(false);
                        }

                        @Override public void onError(Throwable throwable) {
                            mSellingView.setLoadingProgressIndicator(false);
                            handleError(throwable);
                        }

                        @Override public void onNext(PaginatedList<Advert> paginatedList) {
                            mAdvertPaginatedList = paginatedList;
                            mSellingView.showLoadedAdvertsInView(paginatedList.getResults());
                        }
                    });
            mSubscriptions.add(subscription);
        }
    }

    private void handleError(Throwable throwable) {
        e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mSellingView.showNetworkConnectionError();
        } else {
            mSellingView.showUnknownError();
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
