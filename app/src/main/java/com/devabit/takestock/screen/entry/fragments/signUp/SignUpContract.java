package com.devabit.takestock.screen.entry.fragments.signUp;

import com.devabit.takestock.data.model.AuthToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 06/05/2016.
 */
public interface SignUpContract {

    interface View extends BaseView<Presenter> {

        void showUserNameError();

        void showEmailError();

        void showPasswordError();

        void showCredentialsError();

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

        void processAuthToken(AuthToken authToken);

    }

    interface Presenter extends BasePresenter {

        void obtainAccessToken(UserCredentials userCredentials);
    }
}
