package com.devabit.takestock.screen.main;

import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
interface MainContract {

    interface View extends BaseView<Presenter> {

        void showDataUpdated();

        void showLoadingDataError();

        void showNetworkConnectionError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void updateData(int userId);
    }
}
