package com.devabit.takestock.screens.profile.edit;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.util.Logger;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class ProfileEditPresenter implements ProfileEditContract.Presenter {

    private static final String TAG = Logger.makeLogTag(ProfileEditPresenter.class);

    private final DataRepository mDataRepository;
    private final ProfileEditContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    public ProfileEditPresenter(@NonNull DataRepository dataRepository, @NonNull ProfileEditContract.View profileView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileEditPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void updateUser(User user) {

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
