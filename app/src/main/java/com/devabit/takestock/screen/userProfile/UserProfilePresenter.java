package com.devabit.takestock.screen.userProfile;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.PaginatedList;
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
 * Created by Victor Artemyev on 12/10/2016.
 */

final class UserProfilePresenter implements UserProfileContract.Presenter {

    private final User mUser;
    private final DataRepository mDataRepository;
    private final UserProfileContract.View mView;

    private CompositeSubscription mSubscriptions;

    UserProfilePresenter(@NonNull User user, @NonNull DataRepository dataRepository, @NonNull UserProfileContract.View buyingView) {
        mUser = user;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(buyingView, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(UserProfilePresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void loadAdvert() {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getPaginatedAdvertListWithFilter(createAdvertFilter())
                .compose(RxTransformers.<PaginatedList<Advert>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Advert>>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mView.setProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(PaginatedList<Advert> paginatedList) {
                        mView.showAdvertsInView(paginatedList.getResults());
                    }
                });
        mSubscriptions.add(subscription);
    }

    private AdvertFilter createAdvertFilter() {
        return new AdvertFilter.Builder()
                .setAuthorId(mUser.getId())
                .setOrder(AdvertFilter.ORDER_UPDATED_AT_DESCENDING)
                .create();
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mView.showNetworkConnectionError();
        } else {
            mView.showUnknownError();
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
