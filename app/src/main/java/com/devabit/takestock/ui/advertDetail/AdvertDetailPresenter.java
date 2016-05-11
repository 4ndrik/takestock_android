package com.devabit.takestock.ui.advertDetail;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.data.source.DataRepository;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class AdvertDetailPresenter implements AdvertDetailContract.Presenter {

    private static final String TAG = makeLogTag(AdvertDetailPresenter.class);

    private final DataRepository mDataRepository;
    private final AdvertDetailContract.View mAdvertView;

    private CompositeSubscription mSubscriptions;

    public AdvertDetailPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertDetailContract.View advertView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mAdvertView = checkNotNull(advertView, "sellingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mAdvertView.setPresenter(AdvertDetailPresenter.this);
    }


    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void fetchShippingById(int id) {
        Shipping shipping = mDataRepository.getShippingById(id);
        mAdvertView.showShippingInView(shipping);
    }

    @Override public void fetchCertificationById(int id) {
        Certification certification = mDataRepository.getCertificationById(id);
        mAdvertView.showCertificationInView(certification);
    }

    @Override public void fetchConditionById(int id) {
        Condition condition = mDataRepository.getConditionById(id);
        mAdvertView.showConditionInView(condition);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
