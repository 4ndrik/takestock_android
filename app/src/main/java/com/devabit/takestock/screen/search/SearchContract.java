package com.devabit.takestock.screen.search;

import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showAdvertsCountInView(int count);

        void showAdvertsInView(List<Advert> adverts);

        void showAdvertAddedToWatching(int advertId);

        void showAdvertRemovedFromWatching(int advertId);

        void showAdvertWatchingError(int advertId);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void refreshAdverts();

        void fetchAdverts();

        void fetchAdvertsPerFilter(AdvertFilter filter);

        void addRemoveWatchingAdvert(int advertId);
    }
}
