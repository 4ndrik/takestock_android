package com.devabit.takestock.screens.advert.preview;

import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.models.Shipping;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public interface AdvertPreviewContract {

    interface View extends BaseView<Presenter> {

        void showShippingInView(Shipping shipping);

        void showCertificationInView(Certification certification);

        void showConditionInView(Condition condition);

    }

    interface Presenter extends BasePresenter {
        void fetchShippingById(int id);

        void fetchCertificationById(int id);

        void fetchConditionById(int id);
    }
}
