package com.devabit.takestock.screen.entry.fragments.signIn;

import com.devabit.takestock.data.models.AuthToken;
import com.devabit.takestock.data.models.UserCredentials;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public interface SignInContract {

    interface View extends BaseView<Presenter> {

        void showUserNameError();

        void showPasswordError();

        void showNetworkConnectionError();

        void showCredentialsError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

        void processAuthToken(AuthToken authToken);
    }

    interface Presenter extends BasePresenter {

        void obtainAuthToken(UserCredentials userCredentials);

    }
}
