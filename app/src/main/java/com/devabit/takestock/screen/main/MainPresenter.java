package com.devabit.takestock.screen.main;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func8;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.utils.Logger.*;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = makeLogTag(MainPresenter.class);

    private final DataRepository mDataRepository;
    private final MainContract.View mMainView;

    private CompositeSubscription mSubscriptions;

    private boolean mIsDataLoaded;

    public MainPresenter(@NonNull DataRepository dataRepository, @NonNull MainContract.View mainView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mMainView = checkNotNull(mainView, "mainView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mMainView.setPresenter(MainPresenter.this);
    }

    @Override public void resume() {
        updateData();
    }

    @Override public void updateData() {
        if (mIsDataLoaded) return;
        mMainView.setProgressIndicator(true);
        Subscription subscription = Observable
                .zip(
                        mDataRepository.refreshCategories(),
                        mDataRepository.refreshCertifications(),
                        mDataRepository.refreshConditions(),
                        mDataRepository.refreshOfferStatuses(),
                        mDataRepository.refreshPackagings(),
                        mDataRepository.refreshShippings(),
                        mDataRepository.refreshSizes(),
                        mDataRepository.updateBusinessTypes(),
                        new Func8<List<Category>, List<Certification>, List<Condition>, List<OfferStatus>, List<Packaging>, List<Shipping>, List<Size>, List<BusinessType>, Void>() {
                            @Override public Void call(List<Category> categories, List<Certification> certifications, List<Condition> conditionList, List<OfferStatus> offerStatuses, List<Packaging> packagings, List<Shipping> shippings, List<Size> sizes, List<BusinessType> businessTypes) {
                                return null;
                            }
                        }
                )
                .compose(RxTransformers.<Void>applyObservableSchedulers())
                .subscribe(new Subscriber<Void>() {
                    @Override public void onCompleted() {
                        mIsDataLoaded = true;
                        mMainView.setProgressIndicator(false);
                        mMainView.showDataUpdated();
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mMainView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mMainView.showNetworkConnectionError();
                        } else {
                            mMainView.showLoadingDataError();
                        }
                    }

                    @Override public void onNext(Void v) {
                        LOGD(TAG, "BusinessTypes updated: ");
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
