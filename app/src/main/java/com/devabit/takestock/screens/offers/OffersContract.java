package com.devabit.takestock.screens.offers;

import android.util.Pair;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public interface OffersContract {

    interface View extends BaseView<Presenter> {

        void showOffersCounterOfferPairsInView(List<Pair<Offer, Offer>> pairs);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

        void showUpdatedOfferInView(Offer offer);

        void showSavedCounterOfferInView(Offer offer);
    }

    interface Presenter extends BasePresenter {

        void fetchOffersByAdvertId(int advertId);

        void updateOffer(Offer offer);

        void saveCounterOffer(Offer offer);

        void rejectOffer(Offer offer);
    }
}
