package com.devabit.takestock.screens.advert.detail;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.Shipping;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

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
        mAdvertView = checkNotNull(advertView, "sellingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mAdvertView.setPresenter(AdvertDetailPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void fetchShippingById(int id) {
        Shipping shipping = mDataRepository.getShippingById(id);
        if (shipping == null) return;
        mAdvertView.showShippingInView(shipping);
    }

    @Override public void fetchCertificationById(int id) {
        Certification certification = mDataRepository.getCertificationById(id);
        if (certification == null) return;
        mAdvertView.showCertificationInView(certification);
    }

    @Override public void fetchConditionById(int id) {
        Condition condition = mDataRepository.getConditionById(id);
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
