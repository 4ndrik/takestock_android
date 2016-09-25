package com.devabit.takestock.screen.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.BusinessSubtype;
import com.devabit.takestock.data.model.BusinessType;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.profile.edit.adapter.BusinessSubtypeSpinnerAdapter;
import com.devabit.takestock.screen.profile.edit.adapter.BusinessTypeSpinnerAdapter;
import com.devabit.takestock.screen.profile.edit.dialog.ProfilePhotoPickerDialog;
import com.devabit.takestock.utils.ImagePickerUtil;
import com.devabit.takestock.widget.HintSpinnerAdapter;
import timber.log.Timber;

import java.io.File;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/06/2016.
 */
public class ProfileEditActivity extends AppCompatActivity implements ProfileEditContract.View {

    public static Intent getStartIntent(Context context, User user) {
        Intent starter = new Intent(context, ProfileEditActivity.class);
        starter.putExtra(User.class.getName(), user);
        return starter;
    }

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.profile_image_view) protected ImageView mProfileImageView;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;
    @BindView(R.id.business_name_edit_text) protected EditText mBusinessNameEditText;
    @BindView(R.id.postcode_edit_text) protected EditText mPostcodeEditText;
    @BindView(R.id.vat_number_edit_text) protected EditText mVatNumberEditText;

    @BindView(R.id.business_type_spinner) protected Spinner mBusinessTypeSpinner;
    @BindView(R.id.business_subtype_spinner) protected Spinner mBusinessSubtypeSpinner;
    @BindView(R.id.business_subtype_text_view) protected TextView mBusinessSubtypeTextView;

    @BindView(R.id.email_subscription_check_box) protected CheckBox mEmailSubscriptionCheckBox;
    @BindView(R.id.vat_register_check_box) protected CheckBox mVatRegisterCheckBox;

    private User mUser;
    private Menu mMenu;
    private String mImageFilePath;

    private ProfileEditContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(ProfileEditActivity.this);
        setUpToolbar();
        User user = getIntent().getParcelableExtra(User.class.getName());
        setUpUser(user);
        setUpVatRegisterCheckBox();
        createPresenter();
    }

    private void createPresenter() {
        new ProfileEditPresenter(
                Injection.provideDataRepository(ProfileEditActivity.this), ProfileEditActivity.this);
    }

    @Override public void setPresenter(@NonNull ProfileEditContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchUserProfileData();
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
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
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
            ImagePickerUtil.openCamera(ProfileEditActivity.this);
        }

        @Override public void pickFromLibrary(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
            ImagePickerUtil.openGallery(ProfileEditActivity.this);
        }

        @Override public void delete(ProfilePhotoPickerDialog dialog) {
            dialog.dismiss();
        }
    };

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePickerUtil.handleActivityResult(requestCode, resultCode, data, ProfileEditActivity.this,
                new ImagePickerUtil.OnImagePickedListener() {
                    @Override public void onImagePicked(File imageFile, String source) {
                        Timber.d("onImagePicked: %s, %s", imageFile.toString(), source);
                        setUpProfileImage(imageFile.getAbsolutePath());
                    }

                    @Override public void onCanceled(String source) {
                        Timber.d("onCanceled: %s", source);
                    }

                    @Override public void onError(Throwable throwable, String source) {
                        Timber.e(throwable, "onError");
                    }
                });
    }

    @Override public void showBusinessTypesInView(List<BusinessType> businessTypes) {
        BusinessTypeSpinnerAdapter businessTypeAdapter = new BusinessTypeSpinnerAdapter(ProfileEditActivity.this, businessTypes);
        final HintSpinnerAdapter<BusinessType> nothingSelectedAdapter = new HintSpinnerAdapter<>(
                businessTypeAdapter, R.layout.item_spinner, R.string.advert_create_select_one, ProfileEditActivity.this);
        mBusinessTypeSpinner.setAdapter(nothingSelectedAdapter);
        mBusinessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BusinessType type = nothingSelectedAdapter.getItem(position);
                if (type == null) return;
                showBusinessSubtypesInView(type.getSubtypes());
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setUserBusinessTypeSelection(nothingSelectedAdapter, businessTypes);
    }

    private void setUserBusinessTypeSelection(HintSpinnerAdapter<BusinessType> adapter, List<BusinessType> types) {
        BusinessType type = getBusinessTypeById(mUser.getBusinessTypeId(), types);
        if (type == null) return;
        int position = adapter.getPosition(type);
        mBusinessTypeSpinner.setSelection(position);
    }

    private BusinessType getBusinessTypeById(int id, List<BusinessType> types) {
        if (id <= 0) return null;
        for (BusinessType type : types) {
            if (type.getId() == id) return type;
        }
        return null;
    }

    private void showBusinessSubtypesInView(List<BusinessSubtype> subtypes) {
        if (subtypes.isEmpty()) {
            mBusinessSubtypeSpinner.setAdapter(null);
            setBusinessSubtypeContentVisibility(false);
        } else {
            BusinessSubtypeSpinnerAdapter adapter = new BusinessSubtypeSpinnerAdapter(ProfileEditActivity.this, subtypes);
            HintSpinnerAdapter<BusinessSubtype> nothingSelectedAdapter
                    = new HintSpinnerAdapter<>(
                    adapter, R.layout.item_spinner, R.string.advert_create_select_one, ProfileEditActivity.this);
            mBusinessSubtypeSpinner.setAdapter(nothingSelectedAdapter);
            setUserBusinessSubtypesTypeSelection(nothingSelectedAdapter, subtypes);
            setBusinessSubtypeContentVisibility(true);
        }
    }

    private void setUserBusinessSubtypesTypeSelection(HintSpinnerAdapter<BusinessSubtype> adapter,
                                                      List<BusinessSubtype> subtypes) {
        BusinessSubtype subtype = getBusinessSubtypeById(mUser.getBusinessSubtypeId(), subtypes);
        if (subtype == null) return;
        int position = adapter.getPosition(subtype);
        mBusinessSubtypeSpinner.setSelection(position);
    }

    private BusinessSubtype getBusinessSubtypeById(int id, List<BusinessSubtype> subtypes) {
        if (id <= 0) return null;
        for (BusinessSubtype subtype : subtypes) {
            if (subtype.getId() == id) return subtype;
        }
        return null;
    }

    private void setBusinessSubtypeContentVisibility(boolean visible) {
        mBusinessSubtypeTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mBusinessSubtypeSpinner.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setUpProfileImage(String filePath) {
        mImageFilePath = filePath;
        Glide.with(ProfileEditActivity.this)
                .load(mImageFilePath)
                .error(R.drawable.ic_placeholder_user_96dp)
                .crossFade()
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
        user.setPhotoPath(getImageFilePath());
        user.setBusinessName(getBusinessName());
        user.setBusinessTypeId(getBusinessTypeId());
        user.setBusinessSubtypeId(getBusinessSubtypeId());
        user.setSubscribed(isEmailSubscribed());
        user.setVatExempt(isVatRegistered());
        return user;
    }

    private String getName() {
        return mUserNameEditText.getText().toString().trim();
    }

    private String getEmail() {
        return mEmailEditText.getText().toString().trim();
    }

    private String getBusinessName() {
        return mBusinessNameEditText.getText().toString().trim();
    }

    private String getImageFilePath() {
        return mImageFilePath.equals(mUser.getPhotoPath()) ? "" : mImageFilePath;
    }

    private int getBusinessTypeId() {
        BusinessType type = (BusinessType) mBusinessTypeSpinner.getSelectedItem();
        return type == null ? -1 : type.getId();
    }

    private int getBusinessSubtypeId() {
        BusinessSubtype subtype = (BusinessSubtype) mBusinessSubtypeSpinner.getSelectedItem();
        return subtype == null ? -1 : subtype.getId();
    }

    private boolean isEmailSubscribed() {
        return mEmailSubscriptionCheckBox.isChecked();
    }

    private boolean isVatRegistered() {
        return mVatRegisterCheckBox.isChecked();
    }

    @Override public void showUserUpdatedInView(User user) {
        setUpUser(user);
    }

    private void setUpUser(User user) {
        mUser = user;
        setUpProfileImage(user.getPhotoPath());
        mUserNameEditText.setText(mUser.getUserName());
        mEmailEditText.setText(mUser.getEmail());
        mEmailSubscriptionCheckBox.setChecked(mUser.isSubscribed());
        mBusinessNameEditText.setText(mUser.getBusinessName());
        mPostcodeEditText.setText(String.valueOf(mUser.getPostcode()));
        if (mUser.isVatExempt()) {
            mVatRegisterCheckBox.setChecked(true);
            setVatNumberEditTextVisibility(true);
            mVatNumberEditText.setText(String.valueOf(mUser.getVatNumber()));
        } else {
            setVatNumberEditTextVisibility(false);
        }
    }

    private void setUpVatRegisterCheckBox() {
        mVatRegisterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setVatNumberEditTextVisibility(isChecked);
            }
        });
    }

    private void setVatNumberEditTextVisibility(boolean visible) {
        mVatNumberEditText.setVisibility(visible ? View.VISIBLE : View.GONE);
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
        setMenuVisibility(!isActive);
        setProgressBarActive(isActive);
        setTouchDisabled(isActive);
        setContentInputTransparency(isActive);
    }

    private void setMenuVisibility(boolean isActive) {
        mMenu.getItem(0).setVisible(isActive);
    }

    private void setProgressBarActive(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
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

    private void setContentInputTransparency(boolean isActive) {
        for (int i = 0; i < mContentInput.getChildCount(); i++) {
            View view = mContentInput.getChildAt(i);
            view.setAlpha(isActive ? 0.5f : 1.0f);
        }
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
