package com.devabit.takestock.screen.offer;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

interface OfferContract {

    interface View extends BaseView<Presenter> {

        void showOfferAcceptedInView(Offer offer);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void acceptOffer(Offer offer, Offer.Accept accept);
    }
}
