package com.devabit.takestock.screen.shipping;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 05/10/2016.
 */

interface ShippingContract {

    interface View extends BaseView<Presenter> {

        void showOfferAcceptedInView(Offer offer);

        void showHouseError();

        void showStreetError();

        void showCityError();

        void showPostcodeError();

        void showPhoneError();

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void acceptOffer(Offer offer, Offer.Accept accept);
    }
}
