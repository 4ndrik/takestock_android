package com.devabit.takestock.screens.entry.fragments.signUp;

import com.devabit.takestock.data.models.AuthToken;
import com.devabit.takestock.data.models.UserCredentials;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 06/05/2016.
 */
public interface SignUpContract {

    interface View extends BaseView<Presenter> {

        void showIncorrectUserNameError();

        void showIncorrectEmailAddressError();

        void showIncorrectPasswordError();

        void showIncorrectCredentialsError();

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

        void processAuthToken(AuthToken authToken);

    }

    interface Presenter extends BasePresenter {

        void obtainAccessToken(UserCredentials userCredentials);
    }
}
