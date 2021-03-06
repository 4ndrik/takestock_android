package com.devabit.takestock.screen.profileEditor;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.BusinessSubtype;
import com.devabit.takestock.data.model.BusinessType;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.profileEditor.adapter.BusinessSubtypeSpinnerAdapter;
import com.devabit.takestock.screen.profileEditor.adapter.BusinessTypeSpinnerAdapter;
import com.devabit.takestock.screen.profileEditor.dialog.PasswordDialog;
import com.devabit.takestock.screen.profileEditor.dialog.PhotoPickerDialog;
import com.devabit.takestock.utils.ImagePicker;
import com.devabit.takestock.ui.widget.HintSpinnerAdapter;
import timber.log.Timber;

import java.io.File;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/06/2016.
 */
public class ProfileEditorActivity extends AppCompatActivity implements ProfileEditorContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProfileEditorActivity.class);
    }

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.profile_image_view) protected ImageView mProfileImageView;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;
    @BindView(R.id.password_edit_text) protected EditText mPasswordEditText;
    @BindView(R.id.business_name_edit_text) protected EditText mBusinessNameEditText;
    @BindView(R.id.postcode_edit_text) protected EditText mPostcodeEditText;
    @BindView(R.id.vat_number_edit_text) protected EditText mVatNumberEditText;

    @BindView(R.id.business_type_spinner) protected Spinner mBusinessTypeSpinner;
    @BindView(R.id.business_subtype_spinner) protected Spinner mBusinessSubtypeSpinner;
    @BindView(R.id.business_subtype_text_view) protected TextView mBusinessSubtypeTextView;

    @BindView(R.id.email_subscription_check_box) protected CheckBox mEmailSubscriptionCheckBox;

    private TakeStockAccount mAccount;
    private Menu mMenu;
    private String mImageFilePath;

    private ProfileEditorContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        ButterKnife.bind(ProfileEditorActivity.this);
        setUpToolbar();
        mAccount = TakeStockAccount.get(ProfileEditorActivity.this);
        bindAccount(mAccount);
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(ProfileEditorActivity.this, R.id.toolbar);
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

    private void updateUser() {
        mPresenter.updateUser(createUser());
    }

    private User createUser() {
        return new User.Builder()
                .setId(mAccount.getId())
                .setUserName(getName())
                .setEmail(getEmail())
                .setIsSubscribed(isEmailSubscribed())
                .setBusinessName(getBusinessName())
                .setPhoto(getPhoto())
                .setPostcode(getPostcode())
                .setVatNumber(getVatNumber())
                .setBusinessTypeId(getBusinessTypeId())
                .setBusinessSubtypeId(getBusinessSubtypeId())
                .build();
    }

    private void bindAccount(TakeStockAccount account) {
        setUpProfileImage(account.getPhoto());
        mUserNameEditText.setText(account.getName());
        mEmailEditText.setText(account.getEmail());
        mPasswordEditText.setText(account.getPassword());
        mEmailSubscriptionCheckBox.setChecked(account.isSubscribed());
        mBusinessNameEditText.setText(account.getBusinessName());
        mPostcodeEditText.setText(account.getPostcode());
        mVatNumberEditText.setText(account.getVatNumber());
    }

    private void createPresenter() {
        new ProfileEditorPresenter(Injection.provideDataRepository(ProfileEditorActivity.this), ProfileEditorActivity.this);
    }

    @Override public void setPresenter(@NonNull ProfileEditorContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadBusinessTypes();
    }

    @OnClick(R.id.profile_image_view)
    protected void onProfileImageViewClick() {
        displayPhotoPickerDialog();
    }

    private void displayPhotoPickerDialog() {
        PhotoPickerDialog dialog = PhotoPickerDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setOnPickPhotoListener(mPickPhotoListener);
    }

    private final PhotoPickerDialog.OnPickPhotoListener mPickPhotoListener
            = new PhotoPickerDialog.OnPickPhotoListener() {
        @Override public void pickFromCamera(PhotoPickerDialog dialog) {
            dialog.dismiss();
            ImagePicker.openCamera(ProfileEditorActivity.this);
        }

        @Override public void pickFromLibrary(PhotoPickerDialog dialog) {
            dialog.dismiss();
            ImagePicker.openGallery(ProfileEditorActivity.this);
        }

        @Override public void delete(PhotoPickerDialog dialog) {
            dialog.dismiss();
        }
    };

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePicker.handleActivityResult(requestCode, resultCode, data, ProfileEditorActivity.this,
                new ImagePicker.OnImagePickedListener() {
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
        BusinessTypeSpinnerAdapter businessTypeAdapter = new BusinessTypeSpinnerAdapter(ProfileEditorActivity.this, businessTypes);
        final HintSpinnerAdapter<BusinessType> nothingSelectedAdapter = new HintSpinnerAdapter<>(
                businessTypeAdapter, R.layout.item_spinner, R.string.advert_create_select_one, ProfileEditorActivity.this);
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

    @Override public void showChangedPasswordInView(String password) {
        mAccount.setPassword(password);
        mPasswordEditText.setText(password);
        showSnack(R.string.profile_editor_activity_password_changed);
    }

    private void setUserBusinessTypeSelection(HintSpinnerAdapter<BusinessType> adapter, List<BusinessType> types) {
        BusinessType type = getBusinessTypeById(mAccount.getBusinessTypeId(), types);
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
            BusinessSubtypeSpinnerAdapter adapter = new BusinessSubtypeSpinnerAdapter(ProfileEditorActivity.this, subtypes);
            HintSpinnerAdapter<BusinessSubtype> nothingSelectedAdapter
                    = new HintSpinnerAdapter<>(
                    adapter, R.layout.item_spinner, R.string.advert_create_select_one, ProfileEditorActivity.this);
            mBusinessSubtypeSpinner.setAdapter(nothingSelectedAdapter);
            setUserBusinessSubtypesTypeSelection(nothingSelectedAdapter, subtypes);
            setBusinessSubtypeContentVisibility(true);
        }
    }

    private void setUserBusinessSubtypesTypeSelection(HintSpinnerAdapter<BusinessSubtype> adapter,
                                                      List<BusinessSubtype> subtypes) {
        BusinessSubtype subtype = getBusinessSubtypeById(mAccount.getBusinessSubtypeId(), subtypes);
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
        Glide.with(ProfileEditorActivity.this)
                .load(mImageFilePath)
                .error(R.drawable.ic_placeholder_user_96dp)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mProfileImageView);
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

    private String getPhoto() {
        return mImageFilePath.equals(mAccount.getPhoto()) ? null : mImageFilePath;
    }

    private String getPostcode() {
        return mPostcodeEditText.getText().toString().trim();
    }

    private String getVatNumber() {
        return mVatNumberEditText.getText().toString().trim();
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

    @Override public void showUserUpdatedInView(User user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override public void showPasswordError() {
        showSnack(R.string.profile_editor_activity_password_changed_error);
    }

    @OnClick(R.id.password_edit_text)
    void onPasswordEditTextClick() {
        displayPasswordDialog();
    }

    private void displayPasswordDialog() {
        PasswordDialog dialog = PasswordDialog.newInstance(mAccount.getPassword());
        dialog.setOnPasswordChangeListener(new PasswordDialog.OnPasswordChangeListener() {
            @Override public void onPasswordChanged(String newPassword) {
                mPresenter.changePassword(mAccount.getPassword(), newPassword);
            }
        });
        dialog.show(getFragmentManager(), dialog.getClass().getName());
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
        mPresenter.pause();
        super.onStop();
    }
}
