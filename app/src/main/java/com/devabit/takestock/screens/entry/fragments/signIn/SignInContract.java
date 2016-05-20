package com.devabit.takestock.screens.entry.fragments.signIn;

import com.devabit.takestock.data.models.AuthToken;
import com.devabit.takestock.data.models.UserCredentials;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public interface SignInContract {

    interface View extends BaseView<Presenter> {

        void showIncorrectUsernameError();

        void showIncorrectPasswordError();

        void showNetworkConnectionError();

        void showIncorrectCredentialsError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

        void processAuthToken(AuthToken authToken);
    }

    interface Presenter extends BasePresenter {

        void obtainAuthToken(UserCredentials userCredentials);

    }
}
