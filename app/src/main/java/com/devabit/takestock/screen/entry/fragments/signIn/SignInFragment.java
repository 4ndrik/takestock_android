package com.devabit.takestock.screen.entry.fragments.signIn;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.*;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.AuthToken;
import com.devabit.takestock.data.models.UserCredentials;
import com.devabit.takestock.screen.entry.fragments.signUp.SignUpFragment;
import com.devabit.takestock.screen.entry.fragments.signUp.SignUpPresenter;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 17/05/2016.
 */
public class SignInFragment extends Fragment implements SignInContract.View {

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @BindView(R.id.content_activity_sign_in) protected View mContent;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.user_name_input_layout) protected TextInputLayout mUserNameInputLayout;
    @BindView(R.id.password_input_layout) protected TextInputLayout mPasswordInputLayout;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;

    private Unbinder mUnbinder;
    private SignInContract.Presenter mPresenter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(SignInFragment.this, view);
        setUpToolbar(view);
    }

    private void setUpToolbar(View view) {
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
        hideKeyboard();
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

    @Override public void showUserNameError() {
        mUserNameInputLayout.setErrorEnabled(true);
        mUserNameInputLayout.setError(getString(R.string.error_incorrect_username));
    }

    @OnTextChanged(R.id.user_name_edit_text)
    protected void onUserNameTextChanged() {
        mUserNameInputLayout.setError(null);
        mUserNameInputLayout.setErrorEnabled(false);
    }

    @Override public void showPasswordError() {
        mPasswordInputLayout.setErrorEnabled(true);
        mPasswordInputLayout.setError(getString(R.string.error_incorrect_password));
    }

    @OnTextChanged(R.id.password_edit_text)
    protected void onPasswordTextChanged() {
        mPasswordInputLayout.setError(null);
        mPasswordInputLayout.setErrorEnabled(false);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showCredentialsError() {
        showSnack(R.string.error_incorrect_credentials);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        setProgressBarActive(isActive);
        setTouchDisabled(isActive);
        setContentInputActive(!isActive);
    }

    private void setProgressBarActive(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
    }

    private void setTouchDisabled(boolean isActive) {
        Window window = getActivity().getWindow();
        if (isActive) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void setContentInputActive(boolean isActive) {
        for (int i = 0; i < mContentInput.getChildCount(); i++) {
            View view = mContentInput.getChildAt(i);
            view.setAlpha(isActive ? 1.0f : 0.5f);
        }
    }

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
        hideKeyboard();
        SignUpFragment signUpFragment = SignUpFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_activity_entry, signUpFragment)
                .addToBackStack(null)
                .commit();
        new SignUpPresenter(Injection.provideDataRepository(getActivity()), signUpFragment);
    }

    private void hideKeyboard() {
        Activity parentActivity = getActivity();
        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parentActivity.getWindow().getDecorView().getWindowToken(), 0);
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