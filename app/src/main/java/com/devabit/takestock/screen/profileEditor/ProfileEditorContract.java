package com.devabit.takestock.screen.profileEditor;

import com.devabit.takestock.data.model.BusinessType;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
interface ProfileEditorContract {

    interface View extends BaseView<Presenter> {

        void showBusinessTypesInView(List<BusinessType> businessTypes);

        void showChangedPasswordInView(String password);

        void showUserUpdatedInView(User user);

        void showPasswordError();

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchUserProfileData();

        void changePassword(String currentPass, String newPass);

        void updateUser(User user);
    }
}
