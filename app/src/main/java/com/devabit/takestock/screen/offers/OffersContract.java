package com.devabit.takestock.screen.offers;

import android.util.Pair;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

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
