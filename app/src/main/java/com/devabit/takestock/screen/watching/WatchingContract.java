package com.devabit.takestock.screen.watching;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 24/06/2016.
 */
interface WatchingContract {

    interface View extends BaseView<Presenter> {

        void showRefreshedAdvertsInView(List<Advert> adverts);

        void showLoadedAdvertsInView(List<Advert> adverts);

        void showAdvertRemovedFromWatchingInView(Advert advert);

        void showAdvertRemovedFromWatchingError(Advert advert);

        void showNetworkConnectionError();

        void showUnknownError();

        void setRefreshingProgressIndicator(boolean isActive);

        void setLoadingProgressIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void refreshAdverts();

        void loadAdverts();

        void removeWatchingAdvert(Advert advert);

    }
}
