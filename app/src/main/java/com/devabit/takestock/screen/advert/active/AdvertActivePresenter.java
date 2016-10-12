package com.devabit.takestock.screen.advert.active;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 12/10/2016.
 */

final class AdvertActivePresenter implements AdvertActiveContract.Presenter {

    private final DataRepository mDataRepository;
    private final AdvertActiveContract.View mView;
    private final Advert mAdvert;

    private CompositeSubscription mSubscriptions;

    AdvertActivePresenter(@NonNull Advert advert, @NonNull DataRepository dataRepository, @NonNull AdvertActiveContract.View view) {
        mAdvert = advert;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(AdvertActivePresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void unnotifyAdvert() {
        if (!mAdvert.hasNotifications()) return;
       Subscription subscription = mDataRepository.unnotifyAdvertWithId(mAdvert.getId())
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override public void onNext(Advert advert) {
                        mView.showUnnotifiedAdvertInView(advert);
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
