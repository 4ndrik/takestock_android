package com.devabit.takestock.screen.entry.fragments.signUp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.*;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.AuthToken;
import com.devabit.takestock.data.model.UserCredentials;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 17/05/2016.
 */
public class SignUpFragment extends Fragment implements SignUpContract.View {

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;

    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;

    @BindView(R.id.user_name_input_layout) protected TextInputLayout mUserNameInputLayout;
    @BindView(R.id.email_input_layout) protected TextInputLayout mEmailInputLayout;
    @BindView(R.id.password_input_layout) protected TextInputLayout mPasswordInputLayout;

    @BindView(R.id.agreement_check_box) protected CheckBox mAgreementCheckBox;

    private Unbinder mUnbinder;
    private SignUpContract.Presenter mPresenter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(SignUpFragment.this, view);
        setUpToolbar(view);
        setUpAgreementCheckBox();
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        toolbar.setTitle(R.string.sign_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setUpAgreementCheckBox() {
        String agreement = getString(R.string.agreement);
        String terms = getString(R.string.terms_and_conditions);
        SpannableString text = new SpannableString(agreement + " " + terms);
        text.setSpan(
                new ForegroundColorSpan(
                        ContextCompat.getColor(getActivity(), R.color.grey_600)), 0, agreement.length(), 0);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override public void onClick(View widget) {
                openTermsAndConditions();
            }
        };
        text.setSpan(clickableSpan, text.length() - terms.length(), text.length(), 0);
        mAgreementCheckBox.setText(text);
        mAgreementCheckBox.setMovementMethod(LinkMovementMethod.getInstance());
        mAgreementCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAgreementCheckBox.setError(null);
                }
            }
        });
    }

    private void openTermsAndConditions() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_of_services_url)));
        startActivity(browserIntent);
    }

    @OnClick(R.id.sign_up_button)
    protected void onSignUpButtonClick() {
        hideKeyboard();
        if (validateAgreement()) {
            mPresenter.obtainAccessToken(getUserCredentials());
        }
    }

    private void hideKeyboard() {
        Activity parentActivity = getActivity();
        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parentActivity.getWindow().getDecorView().getWindowToken(), 0);
    }

    private boolean validateAgreement() {
        if (mAgreementCheckBox.isChecked()) {
            return true;
        } else {
            mAgreementCheckBox.setError("");
            return false;
        }
    }

    private UserCredentials getUserCredentials() {
        UserCredentials credentials = new UserCredentials();
        credentials.userName = getUserName();
        credentials.emailAddress = getEmailAddress();
        credentials.password = getPassword();
        return credentials;
    }

    private String getUserName() {
        return mUserNameEditText.getText().toString().trim();
    }

    private String getEmailAddress() {
        return mEmailEditText.getText().toString().trim();
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

    @Override public void showEmailError() {
        mEmailInputLayout.setErrorEnabled(true);
        mEmailInputLayout.setError(getString(R.string.error_incorrect_email));
    }

    @OnTextChanged(R.id.email_edit_text)
    protected void onEmailTextChanged() {
        mEmailInputLayout.setError(null);
        mEmailInputLayout.setErrorEnabled(false);
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

    @Override public void showCredentialsError() {
        showSnack(R.string.error_user_exists);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
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
        userData.putString(getString(R.string.authenticator_user_name), String.valueOf(authToken.username));
        userData.putString(getString(R.string.authenticator_user_email), String.valueOf(authToken.email));
        accountManager.addAccountExplicitly(account, getPassword(), userData);
        accountManager.setAuthToken(account, getString(R.string.authenticator_token_type), authToken.token);
        finishActivityWithResult();
    }

    private void finishActivityWithResult() {
        Activity activity = getActivity();
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    @Override public void setPresenter(@NonNull SignUpContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
