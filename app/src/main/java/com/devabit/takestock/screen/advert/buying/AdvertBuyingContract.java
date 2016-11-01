package com.devabit.takestock.screen.advert.buying;

import android.support.annotation.NonNull;
import android.util.Pair;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

interface AdvertBuyingContract {

    interface View extends BaseView<Presenter> {

        void showOfferAdvertPairInView(Pair<Offer, Advert> offerAdvertPair);

        void showOfferAcceptedInView(Offer offer);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void acceptOffer(Offer offer, Offer.Accept accept);

        void readNotification(@NonNull Notification notification);

        void loadOfferAdvertPair(int offerId, int advertId);
    }
}
