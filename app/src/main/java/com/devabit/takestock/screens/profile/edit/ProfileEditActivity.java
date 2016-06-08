package com.devabit.takestock.screens.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screens.profile.edit.dialogs.ProfilePhotoPickerDialog;
import com.squareup.picasso.Picasso;

import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 08/06/2016.
 */
public class ProfileEditActivity extends AppCompatActivity implements ProfileEditContract.View {

    private static final String TAG = makeLogTag(ProfileEditActivity.class);

    public static Intent getStartIntent(Context context, User user) {
        Intent starter = new Intent(context, ProfileEditActivity.class);
        starter.putExtra(User.class.getName(), user);
        return starter;
    }

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.profile_image_view) protected ImageView mProfileImageView;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;

    private User mUser;
    private Menu mMenu;

    private ProfileEditContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(ProfileEditActivity.this);
        setUpToolbar();
        setUpPresenter();
        User user = getIntent().getParcelableExtra(User.class.getName());
        setUpUser(user);
    }

    private void setUpPresenter() {
        new ProfileEditPresenter(
                Injection.provideDataRepository(ProfileEditActivity.this), ProfileEditActivity.this);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(ProfileEditActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(mMenuItemClickListener);
        toolbar.setTitle(R.string.edit_profile);
        toolbar.inflateMenu(R.menu.profile_edit_menu);
        mMenu = toolbar.getMenu();
    }

    private final Toolbar.OnMenuItemClickListener mMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit_profile:
                    updateUser();
                    return true;

                default:
                    return false;
            }
        }
    };

    private void setUpUser(User user) {
        mUser = user;
        loadProfilePhoto(user.getPhotoPath());
        mUserNameEditText.setText(mUser.getUserName());
        mEmailEditText.setText(mUser.getEmail());
    }

    private void loadProfilePhoto(String photoPath) {
        if (photoPath.isEmpty()) return;
        Picasso.with(ProfileEditActivity.this)
                .load(photoPath)
                .placeholder(R.drawable.placeholder_user_96dp)
                .error(R.drawable.placeholder_user_96dp)
                .centerCrop()
                .fit()
                .into(mProfileImageView);
    }

    private void updateUser() {
        mPresenter.updateUser(getUserForUpdate());
    }

    private User getUserForUpdate() {
        User user = new User();
        user.setId(mUser.getId());
        user.setUserName(getName());
        user.setEmail(getEmail());
        user.setPhotoPath(getPhotoPath());
        user.setDateJoined(mUser.getDateJoined());
        user.setDateLastLogin(mUser.getDateLastLogin());
        user.setSuperuser(mUser.isSuperuser());
        user.setSubscribed(mUser.isSubscribed());
        user.setActive(mUser.isActive());
        user.setVatExempt(mUser.isVatExempt());
        user.setAvgRating(mUser.getAvgRating());
        user.setVerified(mUser.isVerified());
        return user;
    }

    @Override public void showUserUpdatedInView(User user) {

    }

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showUnknownError() {

    }

    @Override public void setProgressIndicator(boolean isActive) {
        setMenuVisibility(!isActive);
        setProgressBarActive(isActive);
    }

    private void setProgressBarActive(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
    }

    private void setMenuVisibility(boolean isActive) {
        mMenu.getItem(0).setVisible(isActive);
    }

    @Override public void setPresenter(@NonNull ProfileEditContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String getName() {
        return mUserNameEditText.getText().toString().trim();
    }

    private String getEmail() {
        return mEmailEditText.getText().toString().trim();
    }

    private String getPhotoPath() {
        return null;
    }

    @OnClick(R.id.profile_image_view)
    protected void onProfileImageViewClick() {
        displayPhotoPickerDialog();
    }

    private void displayPhotoPickerDialog() {
        ProfilePhotoPickerDialog dialog = ProfilePhotoPickerDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setOnPickPhotoListener(mPickPhotoListener);
    }

    private final ProfilePhotoPickerDialog.OnPickPhotoListener mPickPhotoListener
            = new ProfilePhotoPickerDialog.OnPickPhotoListener() {
        @Override public void pickFromCamera(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
        }

        @Override public void pickFromLibrary(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
        }

        @Override public void delete(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
        }
    };

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
