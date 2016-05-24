package com.devabit.takestock.screens.selling;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.ResultList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class SellingPresenter implements SellingContract.Presenter {

    private static final String TAG = makeLogTag(SellingPresenter.class);

    private final DataRepository mDataRepository;
    private final SellingContract.View mSellingView;

    private CompositeSubscription mSubscriptions;
    private ResultList<Advert> mAdvertResultList;

    public SellingPresenter(@NonNull DataRepository dataRepository, @NonNull SellingContract.View sellingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSellingView = checkNotNull(sellingView, "sellingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSellingView.setPresenter(SellingPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void fetchAdvertsPerFilter(AdvertFilter filter) {
        mSellingView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .getResultAdvertListPerFilter(filter)
                .compose(RxTransformers.<ResultList<Advert>>applyObservableSchedulers())
                .subscribe(getSubscriber());
        mSubscriptions.add(subscription);
    }

    @Override public void fetchAdverts() {
        if (mAdvertResultList != null && mAdvertResultList.hasNext()) {
            mSellingView.setProgressIndicator(true);
            Subscription subscription = mDataRepository
                    .getResultAdvertListPerPage(mAdvertResultList.getNext())
                    .compose(RxTransformers.<ResultList<Advert>>applyObservableSchedulers())
                    .subscribe(getSubscriber());
            mSubscriptions.add(subscription);
        }
    }

    private Subscriber<ResultList<Advert>> getSubscriber() {
        return new Subscriber<ResultList<Advert>>() {
            @Override public void onCompleted() {
                mSellingView.setProgressIndicator(false);
            }

            @Override public void onError(Throwable e) {
                mSellingView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", e);
                if (e instanceof NetworkConnectionException) {
                    mSellingView.showNetworkConnectionError();
                } else {
                    mSellingView.showUnknownError();
                }
            }

            @Override public void onNext(ResultList<Advert> resultList) {
                mAdvertResultList = resultList;
                mSellingView.showAdvertsInView(resultList.getResults());
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
