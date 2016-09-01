package com.devabit.takestock.screen.advert.preview;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public interface AdvertPreviewContract {

    interface View extends BaseView<Presenter> {

        void showShippingInView(Shipping shipping);

        void showCertificationInView(Certification certification);

        void showConditionInView(Condition condition);

        void setProgressIndicator(boolean active);

        void showAdvertSaved(Advert advert);

        void showNetworkConnectionError();

        void showUnknownError();

    }

    interface Presenter extends BasePresenter {
        void saveAdvert();
    }
}
