package com.devabit.takestock.screen.profileEditor;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.BusinessType;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import java.util.List;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
class ProfileEditorPresenter implements ProfileEditorContract.Presenter {

    private final DataRepository mDataRepository;
    private final ProfileEditorContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    ProfileEditorPresenter(@NonNull DataRepository dataRepository, @NonNull ProfileEditorContract.View profileView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileEditorPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void fetchUserProfileData() {
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getBusinessTypes()
                .compose(RxTransformers.<List<BusinessType>>applyObservableSchedulers())
                .subscribe(new Subscriber<List<BusinessType>>() {
                    @Override public void onCompleted() {
                        mProfileView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mProfileView.setProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(List<BusinessType> businessTypes) {
                        mProfileView.showBusinessTypesInView(businessTypes);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void changePassword(String currentPass, final String newPass) {
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.changePassword(currentPass, newPass)
                .compose(RxTransformers.<Boolean>applyObservableSchedulers())
                .subscribe(new Subscriber<Boolean>() {
                    @Override public void onCompleted() {
                        mProfileView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mProfileView.setProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(Boolean success) {
                        if (success) {
                            mProfileView.showChangedPasswordInView(newPass);
                        } else {
                            mProfileView.showPasswordError();
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void updateUser(User user) {
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.updateUser(user)
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
                        mProfileView.showUserUpdatedInView(user);
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
