package com.devabit.takestock.screen.entry.fragments.signIn;

import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public interface SignInContract {

    interface View extends BaseView<Presenter> {

        void showSignInSuccess();

        void showUserNameError();

        void showPasswordError();

        void showNetworkConnectionError();

        void showCredentialsError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void signIn(UserCredentials userCredentials);

    }
}
