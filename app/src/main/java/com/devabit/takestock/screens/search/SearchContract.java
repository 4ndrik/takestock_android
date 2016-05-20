package com.devabit.takestock.screens.search;

import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showAdvertsCountInView(int count);

        void showAdvertsInView(List<Advert> adverts);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void refreshAdverts();

        void fetchAdverts();

        void fetchAdvertsPerFilter(AdvertFilter filter);
    }
}
