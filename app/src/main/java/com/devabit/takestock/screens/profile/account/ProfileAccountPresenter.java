package com.devabit.takestock.screens.profile.account;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filters.UserFilter;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.util.Logger;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class ProfileAccountPresenter implements ProfileAccountContract.Presenter {

    private static final String TAG = Logger.makeLogTag(ProfileAccountPresenter.class);

    private final DataRepository mDataRepository;
    private final ProfileAccountContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    public ProfileAccountPresenter(@NonNull DataRepository dataRepository, @NonNull ProfileAccountContract.View profileView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileAccountPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void fetchUserById(int id) {
        mProfileView.setProgressIndicator(true);
        UserFilter filter = new UserFilter();
        filter.setUserId(id);
        Subscription subscription = mDataRepository
                .getUsersPerFilter(filter)
                .flatMap(new Func1<List<User>, Observable<User>>() {
                    @Override public Observable<User> call(List<User> users) {
                        return Observable.from(users);
                    }
                })
                .first()
                .compose(RxTransformers.<User>applyObservableSchedulers())
                .subscribe(new Action1<User>() {
                    @Override public void call(User user) {
                        mProfileView.showUserInView(user);
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
