package com.devabit.takestock.screen.buying;

import android.util.Pair;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
interface BuyingContract {

    interface View extends BaseView<Presenter> {

        void showRefreshedOfferAdvertPairsInView(List<Pair<Offer, Advert>> pairs);

        void showLoadedOfferAdvertPairsInView(List<Pair<Offer, Advert>> pairs);

        void showNetworkConnectionError();

        void showUnknownError();

        void setLoadingProgressIndicator(boolean isActive);

        void setRefreshingProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void refreshOffers();

        void loadOffers();

    }
}
