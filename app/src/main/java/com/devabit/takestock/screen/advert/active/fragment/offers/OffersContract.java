package com.devabit.takestock.screen.advert.active.fragment.offers;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
interface OffersContract {

    interface View extends BaseView<Presenter> {

        void showRefreshedOffersInView(List<Offer> offers);

        void showLoadedOffersInView(List<Offer> offers);

        void showOfferAcceptedInView(Offer offer);

        void showNetworkConnectionError();

        void showUnknownError();

        void setRefreshingProgressIndicator(boolean isActive);

        void setLoadingProgressIndicator(boolean isActive);

        void showUpdatedOfferInView(Offer offer);

    }

    interface Presenter extends BasePresenter {

        void refreshOffers();

        void loadOffers();

        void acceptOffer(Offer offer, Offer.Accept accept);

    }
}
