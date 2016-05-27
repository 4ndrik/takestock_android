package com.devabit.takestock.screens.main;

import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {

        void showDataUpdated();

        void showLoadingDataError();

        void showNetworkConnectionError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void updateData();
    }
}
