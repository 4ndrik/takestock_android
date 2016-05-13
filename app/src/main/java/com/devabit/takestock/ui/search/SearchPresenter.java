package com.devabit.takestock.ui.search;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.ResultList;
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
 * Created by Victor Artemyev on 10/05/2016.
 */
public class SearchPresenter implements SearchContract.Presenter {

    private static final String TAG = makeLogTag(SearchPresenter.class);

    private final DataRepository mDataRepository;
    private final SearchContract.View mSearchView;

    private CompositeSubscription mSubscriptions;

    private ResultList<Advert> mAdvertResultList;

    public SearchPresenter(@NonNull DataRepository dataRepository, @NonNull SearchContract.View searchView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSearchView = checkNotNull(searchView, "searchView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSearchView.setPresenter(SearchPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void refreshAdverts() {
        mAdvertResultList = null;
        fetchAdverts();
    }

    @Override public void fetchAdverts() {
        if (mAdvertResultList == null) {
            mSearchView.setProgressIndicator(true);
            Subscription subscription = mDataRepository
                    .getResultAdvertList()
                    .compose(RxTransformers.<ResultList<Advert>>applyObservableSchedulers())
                    .subscribe(getSubscriber());
            mSubscriptions.add(subscription);
        } else if (mAdvertResultList.hasNext()) {
            mSearchView.setProgressIndicator(true);
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
                mSearchView.setProgressIndicator(false);
            }

            @Override public void onError(Throwable e) {
                mSearchView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", e);
                if (e instanceof NetworkConnectionException) {
                    mSearchView.showNetworkConnectionError();
                } else {
                    mSearchView.showUnknownError();
                }
            }

            @Override public void onNext(ResultList<Advert> resultList) {
                mAdvertResultList = resultList;
                mSearchView.showAdvertsCountInView(resultList.getCount());
                mSearchView.showAdvertsInView(resultList.getResults());
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
