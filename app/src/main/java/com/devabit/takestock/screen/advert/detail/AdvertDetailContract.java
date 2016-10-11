package com.devabit.takestock.screen.advert.detail;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
interface AdvertDetailContract {

    interface View extends BaseView<Presenter> {

        void showAdvertInView(Advert advert);

        void showSimilarAdvertsInView(List<Advert> adverts);

        void showOfferMadeInView(Offer offer);

        void showAdvertAddedToWatching(Advert advert);

        void showAdvertRemovedFromWatching(Advert advert);

        void showAdvertWatchingError(Advert advert);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void loadAdvert();

        void makeOffer(Offer offer);

        void addOrRemoveWatchingAdvert(Advert advert, int userId);
    }
}
