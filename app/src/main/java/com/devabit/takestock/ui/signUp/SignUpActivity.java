package com.devabit.takestock.ui.signUp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.AuthToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.ui.main.MainActivity;
import com.devabit.takestock.util.FontCache;
import com.devabit.takestock.widget.CustomTypefaceSpan;

import java.util.List;

import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 12/04/2016.
 */
public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @BindView(R.id.content_activity_sign_up) protected View mContent;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;

    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;

    @BindViews({R.id.logo_image_view, R.id.user_name_edit_text,
            R.id.password_edit_text, R.id.email_edit_text, R.id.sign_up_button})
    List<View> mViews;

    private SignUpContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(SignUpActivity.this);

        Typeface boldTypeface = FontCache.getTypeface(SignUpActivity.this, R.string.font_brandon_bold);
        Typeface mediumTypeface = FontCache.getTypeface(SignUpActivity.this, R.string.font_brandon_medium);

        Toolbar toolbar = ButterKnife.findById(SignUpActivity.this, R.id.toolbar);
        SpannableString spannableString = new SpannableString(getString(R.string.sign_up));
        spannableString.setSpan(new CustomTypefaceSpan(boldTypeface), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar.setTitle(spannableString);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        Button singUpButton = ButterKnife.findById(SignUpActivity.this, R.id.sign_up_button);
        singUpButton.setTypeface(boldTypeface);

        new SignUpPresenter(
                Injection.provideDataRepository(SignUpActivity.this), SignUpActivity.this);
    }

    @OnClick(R.id.sign_up_button)
    protected void onSignUpButtonClick() {
        mPresenter.obtainAccessToken(getUserCredentials());
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

    @Override public void showIncorrectUserNameError() {
        showSnack(R.string.error_incorrect_username);
    }

    @Override public void showIncorrectEmailAddressError() {
        showSnack(R.string.error_incorrect_email);
    }

    @Override public void showIncorrectPasswordError() {
        showSnack(R.string.error_incorrect_password);
    }

    @Override public void showIncorrectCredentialsError() {
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
        AccountManager accountManager = AccountManager.get(SignUpActivity.this);
        Account account = new Account(getUserName(), getString(R.string.authenticator_account_type));
        accountManager.addAccountExplicitly(account, getPassword(), null);
        accountManager.setAuthToken(account, getString(R.string.authenticator_token_type), authToken.token);

        startActivity(MainActivity.getStartIntent(SignUpActivity.this));
    }

    @Override public void setPresenter(@NonNull SignUpContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
