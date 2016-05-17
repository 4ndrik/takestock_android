package com.devabit.takestock.ui.entry.fragments.signUp;

import com.devabit.takestock.data.model.AuthToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.ui.BasePresenter;
import com.devabit.takestock.ui.BaseView;

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
