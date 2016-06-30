package com.devabit.takestock.screen.buying;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.OfferStatus;
import com.devabit.takestock.data.model.ResultList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class BuyingPresenter implements BuyingContract.Presenter {

    private static final String TAG = makeLogTag(BuyingPresenter.class);

    private final DataRepository mDataRepository;
    private final BuyingContract.View mOffersView;

    private CompositeSubscription mSubscriptions;

    public BuyingPresenter(@NonNull DataRepository dataRepository, @NonNull BuyingContract.View buyingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mOffersView = checkNotNull(buyingView, "buyingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mOffersView.setPresenter(BuyingPresenter.this);
    }

    @Override public void resume() {
        fetchOfferStatuses();
    }

    private void fetchOfferStatuses() {
        Subscription subscription = mDataRepository
                .getOfferStatuses()
                .map(new Func1<List<OfferStatus>, SparseArray<OfferStatus>>() {
                    @Override public SparseArray<OfferStatus> call(List<OfferStatus> statuses) {
                        SparseArray<OfferStatus> result = new SparseArray<>(statuses.size());
                        for (OfferStatus status : statuses) {
                            result.append(status.getId(), status);
                        }
                        return result;
                    }
                })
                .compose(RxTransformers.<SparseArray<OfferStatus>>applyObservableSchedulers())
                .subscribe(new Action1<SparseArray<OfferStatus>>() {
                    @Override public void call(SparseArray<OfferStatus> statuses) {
                        mOffersView.showOfferStatusesInView(statuses);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @Override public void fetchOffersByUserId(int userId) {
        mOffersView.setProgressIndicator(true);
        OfferFilter filter = new OfferFilter();
        filter.setUserId(userId);
        Subscription subscription = mDataRepository
                .getOfferResultListPerFilter(filter)
                .flatMap(new Func1<ResultList<Offer>, Observable<Map<Offer, Advert>>>() {
                    @Override public Observable<Map<Offer, Advert>> call(ResultList<Offer> resultList) {
                        List<Offer> offers = resultList.getResults();
                        return Observable.zip(Observable.just(offers), buildAdvertsPerOffersObservable(offers),
                                new Func2<List<Offer>, SparseArray<Advert>, Map<Offer, Advert>>() {
                                    @Override public Map<Offer, Advert> call(List<Offer> offers, SparseArray<Advert> adverts) {
                                        Map<Offer, Advert> offerAdvertMap = new HashMap<>(offers.size());
                                        for (Offer offer : offers) {
                                            offerAdvertMap.put(offer, adverts.get(offer.getAdvertId()));
                                        }
                                        return offerAdvertMap;
                                    }
                                });
                    }
                })
                .compose(RxTransformers.<Map<Offer, Advert>>applyObservableSchedulers())
                .subscribe(new Action1<Map<Offer, Advert>>() {
                    @Override public void call(Map<Offer, Advert> offerAdvertMap) {
                        mOffersView.showOfferAdvertPairsInView(offerAdvertMap);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    private Observable<SparseArray<Advert>> buildAdvertsPerOffersObservable(List<Offer> offers) {
        return Observable.from(offers)
                .map(new Func1<Offer, Integer>() {
                    @Override public Integer call(Offer offer) {
                        return offer.getAdvertId();
                    }
                })
                .toList()
                .map(new Func1<List<Integer>, AdvertFilter>() {
                    @Override public AdvertFilter call(List<Integer> ids) {
                        AdvertFilter filter = new AdvertFilter();
                        filter.setAdvertIds(ids);
                        return filter;
                    }
                })
                .flatMap(new Func1<AdvertFilter, Observable<List<Advert>>>() {
                    @Override public Observable<List<Advert>> call(AdvertFilter advertFilter) {
                        return mDataRepository.getAdvertsPerFilter(advertFilter);
                    }
                })
                .map(new Func1<List<Advert>, SparseArray<Advert>>() {
                    @Override public SparseArray<Advert> call(List<Advert> adverts) {
                        SparseArray<Advert> result = new SparseArray<>(adverts.size());
                        for (Advert advert : adverts) {
                            result.append(advert.getId(), advert);
                        }
                        return result;
                    }
                });

    }

    @NonNull private Action1<Throwable> getOnError() {
        return new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                mOffersView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", throwable);
                if (throwable instanceof NetworkConnectionException) {
                    mOffersView.showNetworkConnectionError();
                } else {
                    mOffersView.showUnknownError();
                }
            }
        };
    }

    @NonNull private Action0 getOnCompleted() {
        return new Action0() {
            @Override public void call() {
                mOffersView.setProgressIndicator(false);
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
