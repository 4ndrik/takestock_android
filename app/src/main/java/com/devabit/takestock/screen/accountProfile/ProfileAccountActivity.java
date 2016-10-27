package com.devabit.takestock.screen.accountProfile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.TakeStockPref;
import com.devabit.takestock.screen.about.AboutActivity;
import com.devabit.takestock.screen.dialog.emailConfirmation.EmailConfirmationDialog;
import com.devabit.takestock.screen.help.HelpActivity;
import com.devabit.takestock.screen.main.MainActivity;
import com.devabit.takestock.screen.profileEditor.ProfileEditorActivity;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class ProfileAccountActivity extends AppCompatActivity implements ProfileAccountContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProfileAccountActivity.class);
    }

    private static final int RC_EDIT_PROFILE = 101;

    @BindView(R.id.content) ViewGroup mContent;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.profile_image_view) ImageView mProfileImageView;
    @BindView(R.id.profile_name_text_view) TextView mProfileNameTextView;
    @BindView(R.id.rating_bar) RatingBar mRatingBar;

    TakeStockAccount mAccount;
    ProfileAccountContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_account);
        ButterKnife.bind(ProfileAccountActivity.this);
        mAccount = TakeStockAccount.get(ProfileAccountActivity.this);
        setUpToolbar();
        bindAccount();
        createPresenter();
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
        toolbar.setTitle(R.string.navigation_drawer_profile);
    }

    private final Toolbar.OnMenuItemClickListener mMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit_profile:
                    onActionEditProfileClick();
                    return true;

                default:
                    return false;
            }
        }
    };

    private void onActionEditProfileClick() {
        if (mAccount.isVerified()) {
            startProfileEditorActivity();
        } else {
            displayEmailConfirmationDialog();
        }
    }

    private void startProfileEditorActivity() {
        Intent starter = ProfileEditorActivity.getStartIntent(ProfileAccountActivity.this);
        startActivityForResult(starter, RC_EDIT_PROFILE);
    }

    private void displayEmailConfirmationDialog() {
        EmailConfirmationDialog dialog = EmailConfirmationDialog.newInstance(mAccount.getEmail());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void bindAccount() {
        loadProfilePhoto(mAccount.getPhoto());
        mProfileNameTextView.setText(mAccount.getName());
        mRatingBar.setRating(mAccount.getRating());
    }

    private void createPresenter() {
        new ProfileAccountPresenter(
                Injection.provideDataRepository(ProfileAccountActivity.this), ProfileAccountActivity.this);
    }

    @Override public void setPresenter(@NonNull ProfileAccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EDIT_PROFILE && resultCode == RESULT_OK) {
            bindAccount();
            Snackbar.make(mContent, R.string.profile_editor_activity_profile_saved, Snackbar.LENGTH_LONG).show();
        }
    }

    private void loadProfilePhoto(String photoPath) {
        if (photoPath.isEmpty()) return;
        Glide.with(ProfileAccountActivity.this)
                .load(photoPath)
                .error(R.drawable.ic_placeholder_user_96dp)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mProfileImageView);
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
        builder.setTitle(R.string.log_out_dialog_message);
        builder.setPositiveButton(R.string.log_out_dialog_yes, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                logOut();
            }
        });
        builder.setNegativeButton(R.string.log_out_dialog_no, null);
        builder.show();
    }

    private void logOut() {
        mPresenter.logOut(TakeStockPref.getFCMToken(ProfileAccountActivity.this));
    }

    @Override public void showLogOutSuccess() {
        mAccount.removeAccount(new TakeStockAccount.OnAccountRemovedListener() {
            @Override public void onAccountRemoved(boolean isRemoved) {
                if (isRemoved) {
                    startMainActivity();
                }
            }
        });
    }

    private void startMainActivity() {
        startActivity(MainActivity.getStartIntent(ProfileAccountActivity.this, getString(R.string.action_log_out)));
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showLogOutError() {
        showSnack(R.string.profile_account_activity_error_log_out);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
        mContent.setAlpha(isActive ? 0.5f : 1f);
        setTouchDisabled(isActive);
    }

    private void setTouchDisabled(boolean isActive) {
        Window window = getWindow();
        if (isActive) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
