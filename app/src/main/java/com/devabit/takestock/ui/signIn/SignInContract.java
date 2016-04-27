package com.devabit.takestock.ui.signIn;

import com.devabit.takestock.data.model.AccessToken;
import com.devabit.takestock.ui.BasePresenter;
import com.devabit.takestock.ui.BaseView;

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

        void createAccount(AccessToken accessToken);
    }

    interface Presenter extends BasePresenter {

        void obtainAccessToken(String username, String password);

    }
}
