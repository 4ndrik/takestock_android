package com.devabit.takestock.screen.advert.preview;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.model.Condition;
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
public class AdvertPreviewPresenter implements AdvertPreviewContract.Presenter {

    private static final String TAG = makeLogTag(AdvertPreviewPresenter.class);

    private final Advert mAdvert;
    private final DataRepository mDataRepository;
    private final AdvertPreviewContract.View mView;

    private CompositeSubscription mSubscriptions;
    private boolean mIsAdvertDataShown;

    public AdvertPreviewPresenter(@NonNull Advert advert,
                                  @NonNull DataRepository dataRepository,
                                  @NonNull AdvertPreviewContract.View view) {
        mAdvert = checkNotNull(advert, "advert cannot be null.");
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertPreviewPresenter.this);
    }

    @Override public void resume() {
        fetchAdvertData();
    }

    private void fetchAdvertData() {
        if (mIsAdvertDataShown) return;
        fetchShippingWithId(mAdvert.getShippingId());
        fetchCertificationWithId(mAdvert.getCertificationId());
        fetchConditionWithId(mAdvert.getConditionId());
        mIsAdvertDataShown = true;
    }

    private void fetchShippingWithId(int id) {
        Shipping shipping = mDataRepository.getShippingById(id);
        if (shipping == null) return;
        mView.showShippingInView(shipping);
    }

    private void fetchCertificationWithId(int id) {
        Certification certification = mDataRepository.getCertificationById(id);
        if (certification == null) return;
        mView.showCertificationInView(certification);
    }

    private void fetchConditionWithId(int id) {
        Condition condition = mDataRepository.getConditionById(id);
        if (condition == null) return;
        mView.showConditionInView(condition);
    }

    @Override public void saveAdvert() {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveAdvert(mAdvert)
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mView.showNetworkConnectionError();
                        } else {
                            mView.showUnknownError();
                        }
                    }

                    @Override public void onNext(Advert advert) {
                        mView.showAdvertSaved(advert);
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
