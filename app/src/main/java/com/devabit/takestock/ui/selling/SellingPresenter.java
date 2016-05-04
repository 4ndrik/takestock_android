package com.devabit.takestock.ui.selling;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

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

    public SellingPresenter(@NonNull DataRepository dataRepository, @NonNull SellingContract.View sellingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSellingView = checkNotNull(sellingView, "sellingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSellingView.setPresenter(SellingPresenter.this);
    }

    @Override public void create() {
        setUpCategoriesToView();
        setUpShippingToView();
        setUpConditionsToView();
        setUpSizesToView();
        setUpCertificationsToView();
    }

    private void setUpCategoriesToView() {
        Subscription subscription = mDataRepository
                .getCategories()
                .compose(RxTransformers.<List<Category>>applyObservableSchedulers())
                .subscribe(new Action1<List<Category>>() {
                    @Override public void call(List<Category> categories) {
                        mSellingView.showCategoriesInView(categories);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpShippingToView() {
        Subscription subscription = mDataRepository
                .getShippings()
                .compose(RxTransformers.<List<Shipping>>applyObservableSchedulers())
                .subscribe(new Action1<List<Shipping>>() {
                    @Override public void call(List<Shipping> shippings) {
                        mSellingView.showShippingsInView(shippings);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpConditionsToView() {
        Subscription subscription = mDataRepository
                .getConditions()
                .compose(RxTransformers.<List<Condition>>applyObservableSchedulers())
                .subscribe(new Action1<List<Condition>>() {
                    @Override public void call(List<Condition> conditions) {
                        mSellingView.showConditionsInView(conditions);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpSizesToView() {
        Subscription subscription = mDataRepository
                .getSizes()
                .compose(RxTransformers.<List<Size>>applyObservableSchedulers())
                .subscribe(new Action1<List<Size>>() {
                    @Override public void call(List<Size> sizes) {
                        mSellingView.showSizesInView(sizes);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpCertificationsToView() {
        Subscription subscription = mDataRepository
                .getCertifications()
                .compose(RxTransformers.<List<Certification>>applyObservableSchedulers())
                .subscribe(new Action1<List<Certification>>() {
                    @Override public void call(List<Certification> certifications) {
                        mSellingView.showCertificationsInView(certifications);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void resume() {
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
