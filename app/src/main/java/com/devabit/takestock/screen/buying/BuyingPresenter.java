package com.devabit.takestock.screen.buying;

import android.support.annotation.NonNull;
import android.util.Pair;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.PaginatedList;
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
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class BuyingPresenter implements BuyingContract.Presenter {

    private final DataRepository mDataRepository;
    private final BuyingContract.View mView;

    private CompositeSubscription mSubscriptions;
    private PaginatedList<Offer> mOfferPaginatedList;

    BuyingPresenter(@NonNull DataRepository dataRepository, @NonNull BuyingContract.View buyingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(buyingView, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(BuyingPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void refreshOffers() {
        mOfferPaginatedList = null;
        Subscription subscription = mDataRepository
                .getPaginatedOfferListWithFilter(createFilter())
                .flatMap(createOfferAdvertPairsFunc())
                .compose(RxTransformers.<List<Pair<Offer, Advert>>>applyObservableSchedulers())
                .subscribe(new Subscriber<List<Pair<Offer, Advert>>>() {
                    @Override public void onCompleted() {
                        mView.setRefreshingProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setRefreshingProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(List<Pair<Offer, Advert>> pairs) {
                        mView.showRefreshedOfferAdvertPairsInView(pairs);
                    }
                });
        mSubscriptions.add(subscription);

    }

    private OfferFilter createFilter() {
        return new OfferFilter.Builder()
                .setForSelf(true)
                .setOrder(OfferFilter.Order.UPDATED_AT_DESCENDING)
                .setAdditions(OfferFilter.Addition.FROM_BUYER, OfferFilter.Addition.ORIGINAL)
                .setViews(OfferFilter.View.CHILD_OFFERS)
                .create();
    }

    @Override public void loadOffers() {
        if (mOfferPaginatedList != null && mOfferPaginatedList.hasNext()) {
            mView.setLoadingProgressIndicator(true);
            Subscription subscription = mDataRepository
                    .getPaginatedOfferListPerPage(mOfferPaginatedList.getNext())
                    .flatMap(createOfferAdvertPairsFunc())
                    .compose(RxTransformers.<List<Pair<Offer, Advert>>>applyObservableSchedulers())
                    .subscribe(new Action1<List<Pair<Offer, Advert>>>() {
                        @Override public void call(List<Pair<Offer, Advert>> pairs) {
                            mView.setLoadingProgressIndicator(false);
                            mView.showLoadedOfferAdvertPairsInView(pairs);
                        }
                    });
            mSubscriptions.add(subscription);
        }
    }

    @NonNull private Func1<PaginatedList<Offer>, Observable<List<Pair<Offer, Advert>>>> createOfferAdvertPairsFunc() {
        return new Func1<PaginatedList<Offer>, Observable<List<Pair<Offer, Advert>>>>() {
            @Override public Observable<List<Pair<Offer, Advert>>> call(PaginatedList<Offer> paginatedList) {
                return Observable.zip(
                        Observable.just(paginatedList),
                        buildOfferAdvertListObservable(paginatedList.getResults()),
                        new Func2<PaginatedList<Offer>, List<Pair<Offer, Advert>>, List<Pair<Offer, Advert>>>() {
                            @Override public List<Pair<Offer, Advert>> call(PaginatedList<Offer> paginatedList,
                                                                            List<Pair<Offer, Advert>> pairs) {
                                mOfferPaginatedList = paginatedList;
                                return pairs;
                            }
                        });
            }
        };
    }

    private Observable<List<Pair<Offer, Advert>>> buildOfferAdvertListObservable(final List<Offer> offers) {
        return Observable.fromCallable(
                new Callable<AdvertFilter>() {
                    @Override public AdvertFilter call() throws Exception {
                        int[] advertIds = new int[offers.size()];
                        for (int i = 0; i < offers.size(); i++) {
                            advertIds[i] = offers.get(i).getAdvertId();
                        }
                        return new AdvertFilter.Builder()
                                .setAdvertIds(advertIds)
                                .create();
                    }
                })
                .flatMap(new Func1<AdvertFilter, Observable<PaginatedList<Advert>>>() {
                    @Override public Observable<PaginatedList<Advert>> call(AdvertFilter advertFilter) {
                        return mDataRepository.getPaginatedAdvertListWithFilter(advertFilter);
                    }
                })
                .map(new Func1<PaginatedList<Advert>, List<Advert>>() {
                    @Override public List<Advert> call(PaginatedList<Advert> paginatedList) {
                        return paginatedList.getResults();
                    }
                })
                .map(new Func1<List<Advert>, List<Pair<Offer, Advert>>>() {
                    @Override public List<Pair<Offer, Advert>> call(List<Advert> adverts) {
                        List<Pair<Offer, Advert>> pairs = new ArrayList<>(adverts.size());

                        for (int i = 0; i < offers.size(); i++) {
                            Offer offer = offers.get(i);
                            for (Advert advert : adverts) {
                                if (offer.getAdvertId() == advert.getId()) {
                                    pairs.add(Pair.create(offer, advert));
                                    break;
                                }
                            }
                        }
                        return pairs;
                    }
                });
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
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
