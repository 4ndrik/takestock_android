package com.devabit.takestock.screen.accountProfile;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
final class ProfileAccountPresenter implements ProfileAccountContract.Presenter {

    private final DataRepository mDataRepository;
    private final ProfileAccountContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    ProfileAccountPresenter(@NonNull DataRepository dataRepository, @NonNull ProfileAccountContract.View profileView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileAccountPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void logOut(String token) {
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.unregisterDevice(token)
                .flatMap(new Func1<Boolean, Observable<Void>>() {
                    @Override public Observable<Void> call(Boolean unregistered) {
                        if (!unregistered) throw new RuntimeException();
                        return mDataRepository.clearNotifications();
                    }
                })
                .compose(RxTransformers.<Void>applyObservableSchedulers())
                .subscribe(new Subscriber<Void>() {
                    @Override public void onCompleted() {
                        mProfileView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mProfileView.setProgressIndicator(false);
                        mProfileView.showLogOutError();
                        handleError(e);
                    }

                    @Override public void onNext(Void aVoid) {
                        mProfileView.showLogOutSuccess();
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
