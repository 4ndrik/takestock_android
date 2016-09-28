package com.devabit.takestock.screen.offer;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.source.DataRepository;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

class OfferPresenter implements OfferContract.Presenter {

    private final DataRepository mDataRepository;
    private final OfferContract.View mView;

    private CompositeSubscription mSubscriptions;

    OfferPresenter(@NonNull DataRepository dataRepository, @NonNull OfferContract.View view) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(OfferPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void destroy() {

    }
}
