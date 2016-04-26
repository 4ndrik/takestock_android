package com.devabit.takestock.ui.signIn;

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
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;

import com.devabit.takestock.Injection;

import static com.devabit.takestock.util.Logger.LOGD;
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

    @Bind(R.id.content_activity_sign_in) protected View mContent;
    @Bind(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @Bind(R.id.password_edit_text) protected EditText mPasswordEditText;

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
        mPresenter.signIn();
    }

    @Override public String getUserName() {
        return mUserNameEditText.getText().toString().trim();
    }

    @Override public String getPassword() {
        return mPasswordEditText.getText().toString().trim();
    }

    @Override public AccountManager getAccountManager() {
        return AccountManager.get(SignInActivity.this);
    }

    @Override public void showErrorMessage(String message) {
        Snackbar.make(mContent, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        if (isActive) {
            Toast.makeText(this, "start progress", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "stop progress", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void failSignIn() {

    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void successSignIn() {
        LOGD(TAG, "successSignIn");
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }
}
