package com.devabit.takestock.screen.dispatching;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 07/10/2016.
 */

interface DispatchingContract {

    interface View extends BaseView<Presenter> {

        void showOfferAcceptedInView(Offer offer);

        void showArrivalDateError();

        void showPickUpDateError();

        void showTrackingNumberError();

        void showCourierError();

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void acceptOffer(Offer offer, Offer.Accept accept);
    }
}
