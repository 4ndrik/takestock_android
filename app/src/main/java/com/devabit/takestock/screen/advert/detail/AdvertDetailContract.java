package com.devabit.takestock.screen.advert.detail;

import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
interface AdvertDetailContract {

    interface View extends BaseView<Presenter> {

        void showShippingInView(Shipping shipping);

        void showConditionInView(Condition condition);

        void showOfferMadeInView(Offer offer);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchShippingById(int id);

        void fetchConditionById(int id);

        void makeOffer(Offer offer);
    }
}
