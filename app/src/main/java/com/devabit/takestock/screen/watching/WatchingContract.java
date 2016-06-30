package com.devabit.takestock.screen.watching;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 24/06/2016.
 */
public interface WatchingContract {

    interface View extends BaseView<Presenter> {

        void showAdvertsInView(List<Advert> adverts);

        void showAdvertRemovedFromWatchingInView(int advertId);

        void showAdvertRemovedFromWatchingError(int advertId);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void fetchAdverts();

        void removeWatchingAdvert(int advertId);

    }
}
