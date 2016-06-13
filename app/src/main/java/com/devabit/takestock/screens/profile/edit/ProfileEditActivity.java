package com.devabit.takestock.screens.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screens.profile.edit.dialogs.ProfilePhotoPickerDialog;
import com.devabit.takestock.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.devabit.takestock.utils.Logger.makeLogTag;

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

    private static final int REQUEST_CODE_PHOTO_LIBRARY = 101;
    private static final int REQUEST_CODE_PHOTO_CAMERA = 102;

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.profile_image_view) protected ImageView mProfileImageView;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;

    private User mUser;
    private Menu mMenu;
    private Uri mPhotoUri;
    private String mPhotoPath;

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
                case R.id.action_save_profile:
                    updateUser();
                    return true;

                default:
                    return false;
            }
        }
    };

    private void setUpUser(User user) {
        mUser = user;
        loadProfilePhotoFromPath(user.getPhotoPath());
        mUserNameEditText.setText(mUser.getUserName());
        mEmailEditText.setText(mUser.getEmail());
    }

    private void updateUser() {
        Toast.makeText(ProfileEditActivity.this, "Not yet implemented.", Toast.LENGTH_LONG).show();
//        mPresenter.updateUser(getUserForUpdate());
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
            startCameraActivity();
        }

        @Override public void pickFromLibrary(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
            startPhotoLibraryActivity();
        }

        @Override public void delete(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
        }
    };

    private void startCameraActivity() {
        Intent starter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoUri = Uri.fromFile(FileUtil.getPhotoFile());
        starter.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(starter, REQUEST_CODE_PHOTO_CAMERA);
    }

    private void startPhotoLibraryActivity() {
        Intent starter = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(starter, REQUEST_CODE_PHOTO_LIBRARY);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHOTO_LIBRARY && resultCode == RESULT_OK) {
            mPhotoUri = uriFromPhotoLibraryResult(data);
            processPhotoUri(mPhotoUri);
        } else if (requestCode == REQUEST_CODE_PHOTO_CAMERA && resultCode == RESULT_OK) {
            processPhotoUri(mPhotoUri);
        }
    }

    private Uri uriFromPhotoLibraryResult(Intent data) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null)) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return Uri.fromFile(new File(cursor.getString(column_index)));
        }
    }

    private void processPhotoUri(Uri photoUri) {
        mPresenter.processPhotoUriToFile(photoUri, FileUtil.getUniquePhotoFile(ProfileEditActivity.this));
    }

    @Override public void showPhotoInView(String path) {
        loadProfilePhotoFromPath(path);
    }

    private void loadProfilePhotoFromPath(String path) {
        mPhotoPath = path;
        if (mPhotoPath.isEmpty()) return;
        Picasso.with(ProfileEditActivity.this)
                .load(mPhotoPath)
                .placeholder(R.drawable.placeholder_user_96dp)
                .error(R.drawable.placeholder_user_96dp)
                .centerCrop()
                .fit()
                .into(mProfileImageView);
    }

    @Override public void showUserUpdatedInView(User user) {

    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showPhotoError() {
        showSnack(R.string.error_photo);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
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
        return mPhotoPath;
    }


    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
