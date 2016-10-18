package com.devabit.takestock.screen.accountProfile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.screen.about.AboutActivity;
import com.devabit.takestock.screen.help.HelpActivity;
import com.devabit.takestock.screen.main.MainActivity;
import com.devabit.takestock.screen.profileEditor.ProfileEditorActivity;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class ProfileAccountActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProfileAccountActivity.class);
    }

    private static final int RC_EDIT_PROFILE = 101;

    @BindView(R.id.content) ViewGroup mContent;
    @BindView(R.id.profile_image_view) ImageView mProfileImageView;
    @BindView(R.id.profile_name_text_view) TextView mProfileNameTextView;
    @BindView(R.id.rating_bar) RatingBar mRatingBar;

    private TakeStockAccount mAccount;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_account);
        ButterKnife.bind(ProfileAccountActivity.this);
        mAccount = TakeStockAccount.get(ProfileAccountActivity.this);
        setUpToolbar();
        bindAccount();
    }

    private void bindAccount() {
        loadProfilePhoto(mAccount.getPhoto());
        mProfileNameTextView.setText(mAccount.getName());
        mRatingBar.setRating(mAccount.getRating());
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
                    startProfileEditActivity();
                    return true;

                default:
                    return false;
            }
        }
    };

    private void startProfileEditActivity() {
        Intent starter = ProfileEditorActivity.getStartIntent(ProfileAccountActivity.this);
        startActivityForResult(starter, RC_EDIT_PROFILE);
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
        mAccount.removeAccount(new TakeStockAccount.OnAccountRemovedListener() {
            @Override public void onAccountRemoved(boolean isRemoved) {
                if (isRemoved) {
                    startActivity(MainActivity.getStartIntent(
                            ProfileAccountActivity.this, getString(R.string.action_log_out)));
                }
            }
        });
    }
}
