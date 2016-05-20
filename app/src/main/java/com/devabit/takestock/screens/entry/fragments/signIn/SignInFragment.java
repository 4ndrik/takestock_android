package com.devabit.takestock.screens.entry.fragments.signIn;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.*;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.AuthToken;
import com.devabit.takestock.data.models.UserCredentials;
import com.devabit.takestock.screens.entry.fragments.signUp.SignUpFragment;
import com.devabit.takestock.screens.entry.fragments.signUp.SignUpPresenter;

import java.util.List;

import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 17/05/2016.
 */
public class SignInFragment extends Fragment implements SignInContract.View {

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @BindView(R.id.content_activity_sign_in) protected View mContent;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;

    @BindViews({R.id.logo_image_view, R.id.user_name_edit_text, R.id.password_edit_text, R.id.sign_in_button,
            R.id.sign_up_button, R.id.forgot_password_text_view, R.id.new_to_takestock_text_view})
    protected List<View> mViews;

    private Unbinder mUnbinder;
    private SignInContract.Presenter mPresenter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(SignInFragment.this, view);
        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        toolbar.setTitle(R.string.sign_in);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override public void setPresenter(@NonNull SignInContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @OnClick(R.id.sign_in_button)
    protected void onSignInButtonClick() {
        mPresenter.obtainAuthToken(getUserCredentials());
    }

    private UserCredentials getUserCredentials() {
        UserCredentials credentials = new UserCredentials();
        credentials.userName = getUserName();
        credentials.password = getPassword();
        return credentials;
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
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
        ButterKnife.apply(mViews, ENABLED, isActive ? Boolean.FALSE : Boolean.TRUE);
    }

    private static final ButterKnife.Setter<View, Boolean> ENABLED
            = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(@NonNull View view, Boolean isEnable, int index) {
            if (view instanceof Button) {
                view.setEnabled(isEnable);
            } else {
                view.setFocusableInTouchMode(isEnable);
            }
            view.setAlpha(isEnable ? 1.0f : 0.5f);
        }
    };

    @Override public void processAuthToken(AuthToken authToken) {
        AccountManager accountManager = AccountManager.get(getActivity());
        Account account = new Account(getUserName(), getString(R.string.authenticator_account_type));
        Bundle userData = new Bundle();
        userData.putString(getString(R.string.authenticator_user_id), String.valueOf(authToken.userId));
        userData.putString(getString(R.string.authenticator_user_name), authToken.username);
        userData.putString(getString(R.string.authenticator_user_email), authToken.email);
        accountManager.addAccountExplicitly(account, getPassword(), userData);
        accountManager.setAuthToken(account, getString(R.string.authenticator_token_type), authToken.token);
        finishActivityWithResult();
    }

    private void finishActivityWithResult() {
        Activity activity = getActivity();
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    @OnClick(R.id.sign_up_button)
    protected void onSignUpButtonClick() {
        SignUpFragment signUpFragment = SignUpFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_activity_entry, signUpFragment)
                .addToBackStack(null)
                .commit();
        new SignUpPresenter(Injection.provideDataRepository(getActivity()), signUpFragment);
    }

    @Override public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
