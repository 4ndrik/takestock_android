package com.devabit.takestock.screens.profile.account;

import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public interface ProfileAccountContract {

    interface View extends BaseView<Presenter> {

        void showUserInView(User user);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchUserById(int id);

    }
}
