package com.devabit.takestock.ui.search;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.ui.BasePresenter;
import com.devabit.takestock.ui.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showAdvertsInView(List<Advert> adverts);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void fetchAdverts();
    }
}
