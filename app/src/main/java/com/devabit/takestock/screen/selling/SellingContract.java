package com.devabit.takestock.screen.selling;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public interface SellingContract {

    interface View extends BaseView<Presenter> {

        void showRefreshedAdvertsInView(List<Advert> adverts);

        void showLoadedAdvertsInView(List<Advert> adverts);

        void showNetworkConnectionError();

        void showUnknownError();

        void setLoadingProgressIndicator(boolean isActive);

        void setRefreshingProgressIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void refreshAdverts();

        void loadAdverts();
    }
}
