package com.devabit.takestock.screen.advert.detail;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class AdvertDetailPresenter implements AdvertDetailContract.Presenter {

    private static final String TAG = makeLogTag(AdvertDetailPresenter.class);

    private final DataRepository mDataRepository;
    private final AdvertDetailContract.View mAdvertView;

    private CompositeSubscription mSubscriptions;

    public AdvertDetailPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertDetailContract.View advertView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mAdvertView = checkNotNull(advertView, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mAdvertView.setPresenter(AdvertDetailPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void fetchShippingById(int id) {
        Shipping shipping = mDataRepository.getShippingWithId(id);
        if (shipping == null) return;
        mAdvertView.showShippingInView(shipping);
    }

    @Override public void fetchConditionById(int id) {
        Condition condition = mDataRepository.getConditionWithId(id);
        if (condition == null) return;
        mAdvertView.showConditionInView(condition);
    }

    @Override public void makeOffer(Offer offer) {
        mAdvertView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveOffer(offer)
                .compose(RxTransformers.<Offer>applyObservableSchedulers())
                .subscribe(new Subscriber<Offer>() {
                    @Override public void onCompleted() {
                        mAdvertView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mAdvertView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mAdvertView.showNetworkConnectionError();
                        } else {
                            mAdvertView.showUnknownError();
                        }
                    }

                    @Override public void onNext(Offer offer) {
                        mAdvertView.showOfferMade(offer);
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
