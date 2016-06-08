package com.devabit.takestock.screens.profile.edit;

import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public interface ProfileEditContract {

    interface View extends BaseView<Presenter> {

        void showUserUpdatedInView(User user);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void updateUser(User user);
    }
}
