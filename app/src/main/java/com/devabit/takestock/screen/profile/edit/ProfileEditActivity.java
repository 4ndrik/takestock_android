package com.devabit.takestock.screen.profile.edit;

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
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.BusinessSubtype;
import com.devabit.takestock.data.models.BusinessType;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screen.profile.edit.adapter.BusinessSubtypeSpinnerAdapter;
import com.devabit.takestock.screen.profile.edit.adapter.BusinessTypeSpinnerAdapter;
import com.devabit.takestock.screen.profile.edit.dialog.ProfilePhotoPickerDialog;
import com.devabit.takestock.utils.FileUtil;
import com.devabit.takestock.widgets.NothingSelectedSpinnerAdapter;

import java.io.File;
import java.util.List;

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
    @BindView(R.id.content_input) protected ViewGroup mContentInput;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.profile_image_view) protected ImageView mProfileImageView;
    @BindView(R.id.user_name_edit_text) protected EditText mUserNameEditText;
    @BindView(R.id.email_edit_text) protected EditText mEmailEditText;
    @BindView(R.id.business_name_edit_text) protected EditText mBusinessNameEditText;
    @BindView(R.id.postcode_edit_text) protected EditText mPostcodeEditText;
    @BindView(R.id.vat_number_edit_text) protected EditText mVatNumberEditText;
    @BindView(R.id.credit_card_edit_text) protected EditText mCreditCardEditText;

    @BindView(R.id.business_type_spinner) protected Spinner mBusinessTypeSpinner;
    @BindView(R.id.business_subtype_spinner) protected Spinner mBusinessSubtypeSpinner;
    @BindView(R.id.business_subtype_text_view) protected TextView mBusinessSubtypeTextView;

    @BindView(R.id.email_subscription_check_box) protected CheckBox mEmailSubscriptionCheckBox;
    @BindView(R.id.vat_register_check_box) protected CheckBox mVatRegisterCheckBox;

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
        setUpVatRegisterCheckBox();
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

    @Override protected void onStart() {
        super.onStart();
        mPresenter.resume();
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

    @Override public void showBusinessTypesInView(List<BusinessType> businessTypes) {
        BusinessTypeSpinnerAdapter businessTypeAdapter = new BusinessTypeSpinnerAdapter(ProfileEditActivity.this, businessTypes);
        final NothingSelectedSpinnerAdapter<BusinessType> nothingSelectedAdapter = new NothingSelectedSpinnerAdapter<>(
                businessTypeAdapter, R.layout.item_spinner, R.string.select_one, ProfileEditActivity.this);
        mBusinessTypeSpinner.setAdapter(nothingSelectedAdapter);
        mBusinessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BusinessType type = nothingSelectedAdapter.getItem(position);
                showBusinessSubtypesInView(type.getSubtypes());
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setUserBusinessTypeSelection(nothingSelectedAdapter, businessTypes);
    }

    private void setUserBusinessTypeSelection(NothingSelectedSpinnerAdapter<BusinessType> adapter, List<BusinessType> types) {
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
            NothingSelectedSpinnerAdapter<BusinessSubtype> nothingSelectedAdapter
                    = new NothingSelectedSpinnerAdapter<>(
                    adapter, R.layout.item_spinner, R.string.select_one, ProfileEditActivity.this);
            mBusinessSubtypeSpinner.setAdapter(nothingSelectedAdapter);
            setUserBusinessSubtypesTypeSelection(nothingSelectedAdapter, subtypes);
            setBusinessSubtypeContentVisibility(true);
        }
    }

    private void setUserBusinessSubtypesTypeSelection(NothingSelectedSpinnerAdapter<BusinessSubtype> adapter,
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

    @Override public void showPhotoInView(String path) {
        setUpProfilePhoto(path);
    }

    private void setUpProfilePhoto(String photoPath) {
        mPhotoPath = photoPath;
        if (mPhotoPath.isEmpty()) return;
        Glide.with(ProfileEditActivity.this)
                .load(mPhotoPath)
                .error(R.drawable.placeholder_user_96dp)
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
        user.setPhotoPath(getPhotoPath());
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

    private String getPhotoPath() {
        return mPhotoPath.equals(mUser.getPhotoPath()) ? "" : mPhotoPath;
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
        setUpProfilePhoto(user.getPhotoPath());
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

    @Override public void setPresenter(@NonNull ProfileEditContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
