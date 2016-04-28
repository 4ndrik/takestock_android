package com.devabit.takestock.ui.signIn;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.AccessToken;

import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 15/04/2016.
 */
public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    private static final String TAG = makeLogTag(SignInActivity.class);

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @BindView(R.id.content_activity_sign_in) protected View mContent;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;

    private SignInContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(SignInActivity.this);

        new SignInPresenter(
                Injection.provideDataRepository(SignInActivity.this), SignInActivity.this);

    }

    @Override public void setPresenter(@NonNull SignInContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @OnClick(R.id.sign_in_button)
    protected void onSignInButtonClick() {
        mPresenter.obtainAccessToken(getUserName(), getPassword());
    }

    private String getUserName() {
        return mUserNameEditText.getText().toString().trim();
    }

    private String getPassword() {
        return mPasswordEditText.getText().toString().trim();
    }

    @Override public void showIncorrectUsernameError() {
        showSnack(R.string.error_incorrect_username);
    }

    @Override public void showIncorrectPasswordError() {
        showSnack(R.string.error_incorrect_password);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showIncorrectCredentialsError() {
        showSnack(R.string.error_incorrect_credentials);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }


    @Override public void setProgressIndicator(boolean isActive) {

    }

    @Override public void createAccount(AccessToken accessToken) {
        AccountManager accountManager = AccountManager.get(SignInActivity.this);
        Account account = new Account(getUserName(), getString(R.string.authenticator_account_type));
        accountManager.addAccountExplicitly(account, getPassword(), null);
        accountManager.setAuthToken(account, getString(R.string.authenticator_token_type), accessToken.token);

        //TODO: implement start MainActivity


        // remove account
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            boolean isRemoved = accountManager.removeAccountExplicitly(account);
//            LOGD(TAG, "account removed " + isRemoved);
//        } else {
//            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
//                @Override public void run(AccountManagerFuture<Boolean> future) {
//                    try {
//                       LOGD(TAG, "account removed " + future.getResult());
//                    } catch (OperationCanceledException | IOException | AuthenticatorException e) {
//                        LOGE(TAG, "Account removed error", e);
//                    }
//                }
//            }, null);
//        }
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }
}
