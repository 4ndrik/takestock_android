package com.devabit.takestock.screen.accountProfile;

import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
interface ProfileAccountContract {

    interface View extends BaseView<Presenter> {

        void showUserInView(User user);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void loadUser();

    }
}