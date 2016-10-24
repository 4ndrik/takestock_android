package com.devabit.takestock.screen.main;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func8;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import java.util.List;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class MainPresenter implements MainContract.Presenter {

    private final DataRepository mDataRepository;
    private final MainContract.View mMainView;

    private CompositeSubscription mSubscriptions;

    MainPresenter(@NonNull DataRepository dataRepository, @NonNull MainContract.View mainView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mMainView = checkNotNull(mainView, "mainView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mMainView.setPresenter(MainPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void updateData(final int userId) {
        mMainView.setProgressIndicator(true);
        Subscription subscription = Observable
                .zip(
                        mDataRepository.refreshAccountUserWithId(userId),
                        mDataRepository.refreshCategories(),
                        mDataRepository.refreshCertifications(),
                        mDataRepository.refreshConditions(),
                        mDataRepository.refreshPackagings(),
                        mDataRepository.refreshShippings(),
                        mDataRepository.refreshSizes(),
                        mDataRepository.updateBusinessTypes(),
                        new Func8<User, List<Category>, List<Certification>, List<Condition>, List<Packaging>, List<Shipping>, List<Size>, List<BusinessType>, Void>() {
                            @Override public Void call(User user, List<Category> categories, List<Certification> certifications, List<Condition> conditions, List<Packaging> packagings, List<Shipping> shippings, List<Size> sizes, List<BusinessType> businessTypes) {
                                return null;
                            }
                        }
                )
                .compose(RxTransformers.<Void>applyObservableSchedulers())
                .subscribe(new Subscriber<Void>() {
                    @Override public void onCompleted() {
                        mMainView.setProgressIndicator(false);
                        mMainView.showDataUpdated();
                    }

                    @Override public void onError(Throwable throwable) {
                        Timber.e(throwable);
                        mMainView.setProgressIndicator(false);
                        if (throwable instanceof NetworkConnectionException) {
                            mMainView.showNetworkConnectionError();
                        } else {
                            mMainView.showLoadingDataError();
                        }
                    }

                    @Override public void onNext(Void v) {

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void loadNewNotificationsCount() {
        Subscription subscription = mDataRepository.getNewNotificationsCount()
                .compose(RxTransformers.<Integer>applyObservableSchedulers())
                .subscribe(new Action1<Integer>() {
                    @Override public void call(Integer count) {
                        mMainView.showNewNotificationsCount(count);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Timber.e(throwable);
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
