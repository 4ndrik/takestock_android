package com.devabit.takestock.screens.advert.preview;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.models.Shipping;
import com.devabit.takestock.data.source.DataRepository;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class AdvertPreviewPresenter implements AdvertPreviewContract.Presenter {

    private static final String TAG = makeLogTag(AdvertPreviewPresenter.class);

    private final DataRepository mDataRepository;
    private final AdvertPreviewContract.View mAdvertView;

    private CompositeSubscription mSubscriptions;

    public AdvertPreviewPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertPreviewContract.View advertView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mAdvertView = checkNotNull(advertView, "advertView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mAdvertView.setPresenter(AdvertPreviewPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void fetchShippingById(int id) {
        Shipping shipping = mDataRepository.getShippingById(id);
        if (shipping == null) return;
        mAdvertView.showShippingInView(shipping);
    }

    @Override public void fetchCertificationById(int id) {
        Certification certification = mDataRepository.getCertificationById(id);
        if (certification == null) return;
        mAdvertView.showCertificationInView(certification);
    }

    @Override public void fetchConditionById(int id) {
        Condition condition = mDataRepository.getConditionById(id);
        if (condition == null) return;
        mAdvertView.showConditionInView(condition);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
