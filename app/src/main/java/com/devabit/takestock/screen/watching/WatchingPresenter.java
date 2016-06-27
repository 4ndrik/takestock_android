package com.devabit.takestock.screen.watching;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.source.DataRepository;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 24/06/2016.
 */
final class WatchingPresenter implements WatchingContract.Presenter {

    private static final String TAG = makeLogTag(WatchingPresenter.class);

    private final DataRepository mDataRepository;

    private final WatchingContract.View mWatchingView;

    private CompositeSubscription mSubscriptions;

    WatchingPresenter(@NonNull DataRepository dataRepository, @NonNull WatchingContract.View watchingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mWatchingView = checkNotNull(watchingView, "watchingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mWatchingView.setPresenter(WatchingPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void destroy() {

    }
}
