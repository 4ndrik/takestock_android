package com.devabit.takestock.screens.main;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
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

    @Override public void create() {
    }

    @Override public void resume() {
        updateData();
    }

    @Override public void updateData() {
        if (mIsDataLoaded) return;
        mMainView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .updateCategories()
                .flatMap(new Func1<List<Category>, Observable<List<Size>>>() {
                    @Override public Observable<List<Size>> call(List<Category> categories) {
                        LOGD(TAG, "Categories updated: " + categories);
                        return mDataRepository.getSizes();
                    }
                })
                .flatMap(new Func1<List<Size>, Observable<List<Certification>>>() {
                    @Override public Observable<List<Certification>> call(List<Size> sizes) {
                        LOGD(TAG, "Sizes updated: " + sizes);
                        return mDataRepository.updateCertifications();
                    }
                })
                .flatMap(new Func1<List<Certification>, Observable<List<Shipping>>>() {
                    @Override public Observable<List<Shipping>> call(List<Certification> certifications) {
                        LOGD(TAG, "Certifications updated: " + certifications);
                        return mDataRepository.updateShippings();
                    }
                })
                .flatMap(new Func1<List<Shipping>, Observable<List<Condition>>>() {
                    @Override public Observable<List<Condition>> call(List<Shipping> shippings) {
                        LOGD(TAG, "Shippings updated: " + shippings);
                        return mDataRepository.updateConditions();
                    }
                })
                .flatMap(new Func1<List<Condition>, Observable<List<Packaging>>>() {
                    @Override public Observable<List<Packaging>> call(List<Condition> conditions) {
                        LOGD(TAG, "Conditions updated: " + conditions);
                        return mDataRepository.updatePackagings();
                    }
                })
                .flatMap(new Func1<List<Packaging>, Observable<List<OfferStatus>>>() {
                    @Override public Observable<List<OfferStatus>> call(List<Packaging> packagings) {
                        LOGD(TAG, "Packagings updated: " + packagings);
                        return mDataRepository.updateOfferStatuses();
                    }
                })
                .compose(RxTransformers.<List<OfferStatus>>applyObservableSchedulers())
                .subscribe(new Subscriber<List<OfferStatus>>() {
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

                    @Override public void onNext(List<OfferStatus> statuses) {
                        LOGD(TAG, "OfferStatuses updated: " + statuses);
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
