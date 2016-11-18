package com.devabit.takestock.screen.advert.preview;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class AdvertPreviewPresenter implements AdvertPreviewContract.Presenter {

    private final Advert mAdvert;
    private final DataRepository mDataRepository;
    private final AdvertPreviewContract.View mView;

    private CompositeSubscription mSubscriptions;

    AdvertPreviewPresenter(@NonNull Advert advert,
                                  @NonNull DataRepository dataRepository,
                                  @NonNull AdvertPreviewContract.View view) {
        mAdvert = checkNotNull(advert, "advert cannot be null.");
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertPreviewPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void fetchAdvertRelatedData() {
        fetchShippingWithId(mAdvert.getShippingId());
        fetchCertificationWithId(mAdvert.getCertificationId());
        fetchConditionWithId(mAdvert.getConditionId());
    }

    private void fetchShippingWithId(int id) {
        Shipping shipping = mDataRepository.getShippingWithId(id);
        if (shipping == null) return;
        mView.showShippingInView(shipping);
    }

    private void fetchCertificationWithId(int id) {
        Certification certification = mDataRepository.getCertificationWithId(id);
        if (certification == null) return;
        mView.showCertificationInView(certification);
    }

    private void fetchConditionWithId(int id) {
        Condition condition = mDataRepository.getConditionWithId(id);
        if (condition == null) return;
        mView.showConditionInView(condition);
    }

    @Override public void saveAdvert() {
        mView.setProgressIndicator(true);
        Subscription subscription = getAdvertObservable()
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        Timber.e(e);
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

    private Observable<Advert> getAdvertObservable() {
        if (mAdvert.isInDrafts()) {
            mAdvert.setInDrafts(false);
            return mDataRepository.editAdvert(mAdvert);
        } else {
            return mDataRepository.saveAdvert(mAdvert);
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
