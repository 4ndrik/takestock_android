package com.devabit.takestock.ui.signIn;

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
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;

    @BindViews({R.id.logo_image_view, R.id.user_name_edit_text, R.id.password_edit_text, R.id.sign_in_button})
    protected List<View> mViews;

    private SignInContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(SignInActivity.this);

        Typeface boldTypeface = FontCache.getTypeface(SignInActivity.this, R.string.font_brandon_bold);

        Toolbar toolbar = ButterKnife.findById(SignInActivity.this, R.id.toolbar);
        SpannableString spannableString = new SpannableString(getString(R.string.sign_in).toUpperCase());
        spannableString.setSpan(new CustomTypefaceSpan(boldTypeface), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar.setTitle(spannableString);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        Button singInButton = ButterKnife.findById(SignInActivity.this, R.id.sign_in_button);
        singInButton.setTypeface(boldTypeface);

        new SignInPresenter(
                Injection.provideDataRepository(SignInActivity.this), SignInActivity.this);
    }

    @Override public void setPresenter(@NonNull SignInContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override protected void onResume() {
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
        AccountManager accountManager = AccountManager.get(SignInActivity.this);
        Account account = new Account(getUserName(), getString(R.string.authenticator_account_type));
        accountManager.addAccountExplicitly(account, getPassword(), null);
        accountManager.setAuthToken(account, getString(R.string.authenticator_token_type), authToken.token);

        startActivity(MainActivity.getStartIntent(SignInActivity.this));

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
        mPresenter.pause();
    }
}
