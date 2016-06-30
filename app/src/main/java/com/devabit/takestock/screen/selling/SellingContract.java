package com.devabit.takestock.screen.selling;

import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public interface SellingContract {

    interface View extends BaseView<Presenter> {

        void showAdvertsInView(List<Advert> adverts);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void fetchAdvertsPerFilter(AdvertFilter filter);

        void fetchAdverts();
    }
}
