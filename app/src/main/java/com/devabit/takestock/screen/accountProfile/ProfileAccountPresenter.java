package com.devabit.takestock.screen.accountProfile;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
final class ProfileAccountPresenter implements ProfileAccountContract.Presenter {

    private final int mUserId;
    private final DataRepository mDataRepository;
    private final ProfileAccountContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    ProfileAccountPresenter(int userId, @NonNull DataRepository dataRepository, @NonNull ProfileAccountContract.View profileView) {
        mUserId = userId;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileAccountPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void loadUser() {
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getAccountUserWithId(mUserId)
                .compose(RxTransformers.<User>applyObservableSchedulers())
                .subscribe(new Subscriber<User>() {
                    @Override public void onCompleted() {
                        mProfileView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mProfileView.setProgressIndicator(false);
                        handleError(e);

                    }

                    @Override public void onNext(User user) {
                        mProfileView.showUserInView(user);
                    }
                });
        mSubscriptions.add(subscription);
    }


    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mProfileView.showNetworkConnectionError();
        } else {
            mProfileView.showUnknownError();
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
