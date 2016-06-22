package com.devabit.takestock.screen.category;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.Category;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class CategoryPresenter implements CategoryContract.Presenter {

    private static final String TAG = makeLogTag(CategoryPresenter.class);

    private final DataRepository mDataRepository;
    private final CategoryContract.View mCategoriesView;

    private CompositeSubscription mSubscriptions;

    public CategoryPresenter(@NonNull DataRepository dataRepository, @NonNull CategoryContract.View categoriesView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mCategoriesView = checkNotNull(categoriesView, "categoriesView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mCategoriesView.setPresenter(CategoryPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void fetchCategories() {
        mCategoriesView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getCategories()
                .compose(RxTransformers.<List<Category>>applyObservableSchedulers())
                .subscribe(new Subscriber<List<Category>>() {
                    @Override public void onCompleted() {
                        mCategoriesView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mCategoriesView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mCategoriesView.showNetworkConnectionError();
                        } else {
                            mCategoriesView.showUnknownError();
                        }
                    }

                    @Override public void onNext(List<Category> categories) {
                        mCategoriesView.showCategoriesInView(categories);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
