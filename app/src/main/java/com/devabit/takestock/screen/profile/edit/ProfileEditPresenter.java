package com.devabit.takestock.screen.profile.edit;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.BusinessType;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.utils.Logger;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
class ProfileEditPresenter implements ProfileEditContract.Presenter {

    private static final String TAG = Logger.makeLogTag(ProfileEditPresenter.class);

    private final DataRepository mDataRepository;
    private final ProfileEditContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    private boolean mIsUserProfileRelatedDataShowed;

    ProfileEditPresenter(@NonNull DataRepository dataRepository, @NonNull ProfileEditContract.View profileView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileEditPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void fetchUserProfileData() {
        if (mIsUserProfileRelatedDataShowed) return;
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getBusinessTypes()
                .compose(RxTransformers.<List<BusinessType>>applyObservableSchedulers())
                .subscribe(new Action1<List<BusinessType>>() {
                    @Override public void call(List<BusinessType> businessTypes) {
                        mProfileView.showBusinessTypesInView(businessTypes);
                        mIsUserProfileRelatedDataShowed = true;
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @Override public void updateUser(User user) {
        mProfileView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.updateUser(user)
                .compose(RxTransformers.<User>applyObservableSchedulers())
                .doOnNext(new Action1<User>() {
                    @Override public void call(User user) {
                        LOGD(TAG, user);
                    }
                })
                .subscribe(new Action1<User>() {
                    @Override public void call(User user) {
                        mProfileView.showUserUpdatedInView(user);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @NonNull private Action1<Throwable> getOnError() {
        return new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                mProfileView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", throwable);
                if (throwable instanceof NetworkConnectionException) {
                    mProfileView.showNetworkConnectionError();
                } else {
                    mProfileView.showUnknownError();
                }
            }
        };
    }

    @NonNull private Action0 getOnCompleted() {
        return new Action0() {
            @Override public void call() {
                mProfileView.setProgressIndicator(false);
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
