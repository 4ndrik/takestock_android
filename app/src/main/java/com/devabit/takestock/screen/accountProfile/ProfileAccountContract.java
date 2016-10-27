package com.devabit.takestock.screen.accountProfile;

import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
interface ProfileAccountContract {

    interface View extends BaseView<Presenter> {

        void showLogOutSuccess();

        void showNetworkConnectionError();

        void showLogOutError();

        void setProgressIndicator(boolean isActive);


    }

    interface Presenter extends BasePresenter {

        void logOut(String token);

    }
}
