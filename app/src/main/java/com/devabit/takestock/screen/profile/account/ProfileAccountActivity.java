package com.devabit.takestock.screen.profile.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screen.about.AboutActivity;
import com.devabit.takestock.screen.help.HelpActivity;
import com.devabit.takestock.screen.profile.edit.ProfileEditActivity;
import com.squareup.picasso.Picasso;

import static com.devabit.takestock.utils.Logger.*;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class ProfileAccountActivity extends AppCompatActivity implements ProfileAccountContract.View {

    private static final String TAG = makeLogTag(ProfileAccountActivity.class);

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProfileAccountActivity.class);
    }

    private static final int REQUEST_CODE_ACTIVITY_EDIT_PROFILE = 101;

    @BindView(R.id.profile_image_view) protected ImageView mProfileImageView;
    @BindView(R.id.profile_name_text_view) protected TextView mProfileNameTextView;

    private ProfileAccountContract.Presenter mPresenter;

    private AccountManager mAccountManager;
    private Account mAccount;
    private User mUser;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_account);
        ButterKnife.bind(ProfileAccountActivity.this);
        setUpToolbar();
        setUpPresenter();
        mAccountManager = AccountManager.get(ProfileAccountActivity.this);
        mAccount = getAccount(mAccountManager);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(ProfileAccountActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.profile_account_menu);
        toolbar.setOnMenuItemClickListener(mMenuItemClickListener);
        toolbar.setTitle(R.string.profile);
    }

    private final Toolbar.OnMenuItemClickListener mMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit_profile:
                    startProfileEditActivity();
                    return true;

                default:
                    return false;
            }
        }
    };

    private void startProfileEditActivity() {
        Intent starter = ProfileEditActivity.getStartIntent(ProfileAccountActivity.this, mUser);
        startActivityForResult(starter, REQUEST_CODE_ACTIVITY_EDIT_PROFILE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ACTIVITY_EDIT_PROFILE && resultCode == RESULT_OK) {
            User user = data.getParcelableExtra(User.class.getName());
            setUpUser(user);
        }
    }

    private void setUpPresenter() {
        new ProfileAccountPresenter(
                Injection.provideDataRepository(ProfileAccountActivity.this), ProfileAccountActivity.this);
    }

    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        return accounts[0];
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.fetchUserById(getAccountUserId());
    }

    private int getAccountUserId() {
        String userId = mAccountManager.getUserData(mAccount, getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    @Override public void showUserInView(User user) {
        setUpUser(user);
    }

    private void setUpUser(User user) {
        mUser = user;
        loadProfilePhoto(mUser.getPhotoPath());
        mProfileNameTextView.setText(mUser.getUserName());
    }

    private void loadProfilePhoto(String photoPath) {
        if (photoPath.isEmpty()) return;
        Picasso.with(ProfileAccountActivity.this)
                .load(photoPath)
                .placeholder(R.drawable.placeholder_user_96dp)
                .error(R.drawable.placeholder_user_96dp)
                .centerCrop()
                .fit()
                .into(mProfileImageView);
    }

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showUnknownError() {

    }

    @Override public void setProgressIndicator(boolean isActive) {

    }

    @Override public void setPresenter(@NonNull ProfileAccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.help_and_contact_button)
    protected void onHelpButtonClick() {
        startHelpActivity();
    }

    private void startHelpActivity() {
        startActivity(HelpActivity.getStartIntent(ProfileAccountActivity.this));
    }

    @OnClick(R.id.about_button)
    protected void onAboutButtonClick() {
        startAboutActivity();
    }

    private void startAboutActivity() {
        startActivity(AboutActivity.getStartIntent(ProfileAccountActivity.this));
    }


    @OnClick(R.id.log_out_button)
    protected void onLogOutButtonClick() {
        displayLogOutDialog();
    }

    private void displayLogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileAccountActivity.this);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                logOut();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void logOut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            boolean isRemoved = mAccountManager.removeAccountExplicitly(mAccount);
            LOGD(TAG, "account removed " + isRemoved);
            finishActivity();
        } else {
            mAccountManager.removeAccount(mAccount, new AccountManagerCallback<Boolean>() {
                @Override public void run(AccountManagerFuture<Boolean> future) {
                    try {
                        LOGD(TAG, "account removed " + future.getResult());
                        finishActivity();
                    } catch (Exception e) {
                        LOGE(TAG, "Account removed error", e);
                    }
                }
            }, null);
        }
    }

    private void finishActivity() {
        setResult(RESULT_OK);
        finish();
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
