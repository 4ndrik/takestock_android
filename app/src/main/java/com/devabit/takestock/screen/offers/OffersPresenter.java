package com.devabit.takestock.screen.offers;

import android.support.annotation.NonNull;
import android.util.Pair;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class OffersPresenter implements OffersContract.Presenter {

    private static final String TAG = makeLogTag(OffersPresenter.class);

    private final DataRepository mDataRepository;
    private final OffersContract.View mOffersView;

    private CompositeSubscription mSubscriptions;

    public OffersPresenter(@NonNull DataRepository dataRepository, @NonNull OffersContract.View buyingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mOffersView = checkNotNull(buyingView, "buyingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mOffersView.setPresenter(OffersPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void fetchOffersByAdvertId(int advertId) {
        mOffersView.setProgressIndicator(true);
        OfferFilter filter = new OfferFilter();
        filter.setAdvertId(advertId);
        Subscription subscription = mDataRepository
                .getOffersPerFilter(filter)
                .map(new Func1<List<Offer>, List<Pair<Offer, Offer>>>() {
                    @Override public List<Pair<Offer, Offer>> call(List<Offer> offers) {
                        Map<Integer, Offer> offerMap = new HashMap<>();
                        Map<Integer, Offer> counterOfferMap = new HashMap<>();
                        for(Offer offer : offers) {
                            offerMap.put(offer.getId(), offer);
                        }
                        for (Offer offer : offers) {
                            if(offer.getCounterOfferId() > 0) {
                                Offer counterOffer = offerMap.get(offer.getCounterOfferId());
                                counterOfferMap.put(counterOffer.getId(), counterOffer);
                            }
                        }
                        for (Integer id : counterOfferMap.keySet()) {
                            offerMap.remove(id);
                        }
                        List<Pair<Offer, Offer>> result = new ArrayList<>(offerMap.size());
                        for (Integer id : offerMap.keySet()) {
                            Pair<Offer, Offer> pair;
                            Offer offer = offerMap.get(id);
                            int counterOfferId = offer.getCounterOfferId();
                            if (counterOfferId > 0) {
                                pair = Pair.create(offer, counterOfferMap.get(counterOfferId));
                            } else {
                                pair = Pair.create(offer, null);
                            }
                            result.add(pair);
                        }
                        return result;
                    }
                })
                .compose(RxTransformers.<List<Pair<Offer,Offer>>>applyObservableSchedulers())
                .subscribe(new Action1<List<Pair<Offer, Offer>>>() {
                    @Override public void call(List<Pair<Offer, Offer>> pairs) {
                        mOffersView.showOffersCounterOfferPairsInView(pairs);
                    }
                }, getOnError(), getOnCompleted());

        mSubscriptions.add(subscription);
    }

    @Override public void updateOffer(Offer offer) {
        mOffersView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .updateOffer(offer)
                .compose(RxTransformers.<Offer>applyObservableSchedulers())
                .subscribe(new Action1<Offer>() {
                    @Override public void call(Offer offer) {
                        LOGD(TAG, "Updated offer: " + offer);
                        mOffersView.showUpdatedOfferInView(offer);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @Override public void saveCounterOffer(Offer offer) {
        mOffersView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveOffer(offer)
                .compose(RxTransformers.<Offer>applyObservableSchedulers())
                .subscribe(new Action1<Offer>() {
                    @Override public void call(Offer offer) {
                        LOGD(TAG, "Counter offer saved: " + offer);
                        mOffersView.showSavedCounterOfferInView(offer);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @Override public void rejectOffer(Offer offer) {
        updateOffer(offer);
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
