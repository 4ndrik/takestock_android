package com.devabit.takestock.ui.signIn;

import android.accounts.AccountManager;
import com.devabit.takestock.ui.BasePresenter;
import com.devabit.takestock.ui.BaseView;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public interface SignInContract {

    interface View extends BaseView<Presenter> {

        String getUserName();

        String getPassword();

        AccountManager getAccountManager();

        void showErrorMessage(String message);

        void setProgressIndicator(boolean isActive);

        void failSignIn();

        void successSignIn();

    }

    interface Presenter extends BasePresenter {

        void signIn();

    }
}
