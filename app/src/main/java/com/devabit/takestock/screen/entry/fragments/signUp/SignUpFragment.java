package com.devabit.takestock.screen.entry.fragments.signUp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.*;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockPref;
import com.devabit.takestock.data.model.Authentication;
import com.devabit.takestock.data.model.Device;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.screen.dialog.emailConfirmation.EmailConfirmationDialog;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 17/05/2016.
 */
public class SignUpFragment extends Fragment implements SignUpContract.View {

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @BindView(R.id.content) ViewGroup mContent;
    @BindView(R.id.content_input) ViewGroup mContentInput;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.user_name_edit_text) EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) EditText mEmailEditText;
    @BindView(R.id.password_edit_text) EditText mPasswordEditText;
    @BindView(R.id.user_name_input_layout) TextInputLayout mUserNameInputLayout;
    @BindView(R.id.email_input_layout) TextInputLayout mEmailInputLayout;
    @BindView(R.id.password_input_layout) TextInputLayout mPasswordInputLayout;
    @BindView(R.id.terms_text_view) TextView mTermsTextView;

    private Unbinder mUnbinder;
    private SignUpContract.Presenter mPresenter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(SignUpFragment.this, view);
        setUpToolbar(view);
        setUpTermsTextView();
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        toolbar.setTitle(R.string.sign_up_fragment_sign_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setUpTermsTextView() {
        String agreement = getString(R.string.sign_up_fragment_agreement);
        String terms = getString(R.string.sign_up_fragment_term_and_conditions);
        SpannableString text = new SpannableString(agreement + terms);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override public void onClick(View widget) {
                openTermsAndConditions();
            }
        };
        text.setSpan(clickableSpan, text.length() - terms.length(), text.length(), 0);
        mTermsTextView.setText(text);
        mTermsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openTermsAndConditions() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_terms_of_services)));
        startActivity(browserIntent);
    }

    @OnClick(R.id.sign_up_button)
    protected void onSignUpButtonClick() {
        hideKeyboard();
        mPresenter.signUp(getUserCredentials());
    }

    private void hideKeyboard() {
        Activity parentActivity = getActivity();
        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parentActivity.getWindow().getDecorView().getWindowToken(), 0);
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

    @Override public Device getDevice() {
        return new Device.Builder()
                .setId(Build.ID)
                .setName(Build.MODEL)
                .setRegistrationId(TakeStockPref.getFCMToken(getActivity()))
                .setIsActive(true)
                .create();
    }

    @Override public void showAuthTokenInView(Authentication authentication) {
        displayEmailConfirmationDialog(authentication.getEmail());
    }

    private void displayEmailConfirmationDialog(String email) {
        EmailConfirmationDialog dialog = EmailConfirmationDialog.newInstance(email);
        dialog.show(getFragmentManager(), dialog.getClass().getName());
        dialog.setOnOkButtonClickListener(new EmailConfirmationDialog.OnOkButtonClickListener() {
            @Override public void onOkClick() {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

    @Override public void showUserNameError() {
        mUserNameInputLayout.setErrorEnabled(true);
        mUserNameInputLayout.setError(getString(R.string.sign_up_fragment_error_username));
    }

    @OnTextChanged(R.id.user_name_edit_text)
    protected void onUserNameTextChanged() {
        if (mUserNameInputLayout.isErrorEnabled()) {
            mUserNameInputLayout.setError(null);
            mUserNameInputLayout.setErrorEnabled(false);
        }
    }

    @Override public void showEmailError() {
        mEmailInputLayout.setErrorEnabled(true);
        mEmailInputLayout.setError(getString(R.string.error_incorrect_email));
    }

    @OnTextChanged(R.id.email_edit_text)
    protected void onEmailTextChanged() {
        if (mEmailInputLayout.isErrorEnabled()) {
            mEmailInputLayout.setError(null);
            mEmailInputLayout.setErrorEnabled(false);
        }
    }

    @Override public void showPasswordError() {
        mPasswordInputLayout.setErrorEnabled(true);
        mPasswordInputLayout.setError(getString(R.string.error_incorrect_password));
    }

    @OnTextChanged(R.id.password_edit_text)
    protected void onPasswordTextChanged() {
        if (mPasswordInputLayout.isErrorEnabled()) {
            mPasswordInputLayout.setError(null);
            mPasswordInputLayout.setErrorEnabled(false);
        }
    }

    @Override public void showCredentialsError(String error) {
//        showSnack(R.string.error_user_exists);
        Snackbar.make(mContent, error, Snackbar.LENGTH_LONG).show();
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

    @Override public void setPresenter(@NonNull SignUpContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
