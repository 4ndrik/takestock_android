package com.devabit.takestock.screen.adverts;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public interface AdvertsContract {

    interface View extends BaseView<Presenter> {

        void showRefreshedAdvertsInView(List<Advert> adverts);

        void showLoadedAdvertsInView(List<Advert> adverts);

        void showTotalAdvertsCountInView(int count);

        void showAdvertAddedToWatching(int advertId);

        void showAdvertRemovedFromWatching(int advertId);

        void showAdvertWatchingError(int advertId);

        void showNetworkConnectionError();

        void showUnknownError();

        void setLoadingProgressIndicator(boolean isActive);

        void setRefreshingProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void loadAdverts();

        void loadAdvertsWithFilter(@NonNull AdvertFilter filter);

        void refreshAdverts();

        void addOrRemoveWatchingAdvert(int advertId);

    }
}
