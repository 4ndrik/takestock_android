package com.devabit.takestock.ui.selling;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.ui.selling.adapters.*;
import com.devabit.takestock.ui.selling.dialogs.PhotoPickerDialog;
import com.devabit.takestock.util.DateFormats;
import com.devabit.takestock.util.FontCache;
import com.devabit.takestock.widget.CertificationRadioButtonGroupView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.devabit.takestock.util.Logger.*;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class SellingActivity extends AppCompatActivity implements SellingContract.View {

    private static final String TAG = makeLogTag(SellingActivity.class);

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SellingActivity.class);
    }

    @BindView(R.id.content_activity_selling) protected View mContent;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;

    @BindView(R.id.image_gallery_recycler_view) protected RecyclerView mPhotoGalleryRecyclerView;

    @BindView(R.id.category_spinner) protected Spinner mCategorySpinner;
    @BindView(R.id.subcategory_spinner) protected Spinner mSubcategorySpinner;
    @BindView(R.id.packaging_spinner) protected Spinner mPackagingSpinner;
    @BindView(R.id.shipping_spinner) protected Spinner mShippingSpinner;
    @BindView(R.id.condition_spinner) protected Spinner mConditionSpinner;
    @BindView(R.id.size_spinner) protected Spinner mSizeSpinner;

    @BindView(R.id.title_edit_text) protected EditText mTitleEditText;
    @BindView(R.id.item_count_edit_text) protected EditText mItemCountEditText;
    @BindView(R.id.minimum_order_edit_text) protected EditText mMinimumOrderEditText;
    @BindView(R.id.guide_price_edit_text) protected EditText mGuidePriceEditText;
    @BindView(R.id.description_edit_text) protected EditText mDescriptionEditText;
    @BindView(R.id.location_edit_text) protected EditText mLocationEditText;
    @BindView(R.id.size_x_edit_text) protected EditText mSizeXEditText;
    @BindView(R.id.size_y_edit_text) protected EditText mSizeYEditText;
    @BindView(R.id.size_z_edit_text) protected EditText mSizeZEditText;
    @BindView(R.id.certification_extra_edit_text) protected EditText mCertificationExtraEditText;
    @BindView(R.id.tags_edit_text) protected EditText mTagsEditText;

    @BindView(R.id.expiry_date_text_view) protected TextView mExpiryDateTextView;

    @BindView(R.id.certification_group_view) protected CertificationRadioButtonGroupView mCertificationGroupView;

    @BindViews({R.id.preview_ad_button, R.id.save_and_put_on_hold_button})
    List<Button> mButtons;

    private PhotoGalleryAdapter mPhotoGalleryAdapter;

    private SellingContract.Presenter mPresenter;

    private Account mAccount;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);

        new SellingPresenter(
                Injection.provideDataRepository(SellingActivity.this), SellingActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(SellingActivity.this, R.string.font_brandon_bold);
        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(@NonNull Button button, int index) {
                button.setTypeface(boldTypeface);
            }
        });
        setUpToolbar(boldTypeface);
        setUpImageGalleryRecyclerView();

        mExpiryDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                displayDatePickerDialog();
            }
        });

        mPresenter.create();
    }

    private void setUpToolbar(Typeface typeface) {
        mToolbar.inflateMenu(R.menu.main);
        TextView title = ButterKnife.findById(mToolbar, R.id.toolbar_title);
        title.setTypeface(typeface);
        title.setText(R.string.sell_something);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpImageGalleryRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                SellingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mPhotoGalleryRecyclerView.setLayoutManager(layoutManager);
        mPhotoGalleryAdapter = new PhotoGalleryAdapter(SellingActivity.this);
        mPhotoGalleryAdapter.setOnPickPhotoListener(new PhotoGalleryAdapter.OnPickPhotoListener() {
            @Override public void onPick() {
                displayPhotoPickerDialog();
            }
        });
        mPhotoGalleryRecyclerView.setAdapter(mPhotoGalleryAdapter);
    }

    private void displayPhotoPickerDialog() {
        PhotoPickerDialog pickerDialog = PhotoPickerDialog.newInstance();
        pickerDialog.setOnPickListener(new PhotoPickerDialog.OnPickListener() {
            @Override public void onPickFromLibrary(PhotoPickerDialog dialog) {
                dialog.dismiss();
                startPhotoLibraryActivity();
            }

            @Override public void onPickFromCamera(PhotoPickerDialog dialog) {
                dialog.dismiss();
                startPhotoCameraActivity();
            }
        });
        pickerDialog.show(getFragmentManager(), pickerDialog.getClass().getSimpleName());
    }

    private static final int REQUEST_CODE_PHOTO_LIBRARY = 101;

    private void startPhotoLibraryActivity() {
        Intent starter = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(starter, REQUEST_CODE_PHOTO_LIBRARY);
    }

    private static final int REQUEST_CODE_PHOTO_CAMERA = 102;

    private Uri mCameraPhotoUri;

    private void startPhotoCameraActivity() {
        Intent starter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (starter.resolveActivity(getPackageManager()) != null) {
            File publicPictureDirection = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File mediaStorageDir = new File(publicPictureDirection, "TakeStock");
            mCameraPhotoUri = Uri.fromFile(
                    new File(
                            mediaStorageDir.getPath() + File.separator
                                    + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg"));
            starter.putExtra(MediaStore.EXTRA_OUTPUT, mCameraPhotoUri);
        } else {
            showSnack(R.string.error_no_camera_app);
            return;
        }
        startActivityForResult(starter, REQUEST_CODE_PHOTO_CAMERA);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHOTO_LIBRARY && resultCode == RESULT_OK) {
            onPhotoLibraryResult(data);
        } else if (requestCode == REQUEST_CODE_PHOTO_CAMERA && resultCode == RESULT_OK) {
            onPhotoCameraResult();
        }
    }

    private void onPhotoLibraryResult(Intent data) {
        Uri photoUri = data.getData();
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(photoUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            photoUri = Uri.fromFile(new File(cursor.getString(column_index)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        mPresenter.processPhotoToFile(photoUri, getUniquePhotoFile());
    }

    private void onPhotoCameraResult() {
        mPresenter.processPhotoToFile(mCameraPhotoUri, getUniquePhotoFile());
    }

    private File getUniquePhotoFile() {
        return new File(getCacheDir().getPath() + File.separator + UUID.randomUUID().toString() + ".jpg");
    }

    private void displayDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(SellingActivity.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mExpiryDateTextView.setError(null);
                        mExpiryDateTextView.setText(getString(R.string.expiry_date, dayOfMonth, monthOfYear, year));
                    }
                }, 1987, 27, 12);
        dialog.show();
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override public void showEmptyPhotosError() {
        showSnack(R.string.error_empty_photos);
        requestFocusOnView(mPhotoGalleryRecyclerView);
    }

    @Override public void showEmptyTitleError() {
        mTitleEditText.setError(getText(R.string.error_empty_title));
        requestFocusOnView(mTitleEditText);
    }

    @Override public void showEmptyItemCountError() {
        mItemCountEditText.setError(getText(R.string.error_empty_item_count));
        requestFocusOnView(mItemCountEditText);
    }

    @Override public void showEmptyMinimumOrderError() {
        mMinimumOrderEditText.setError(getText(R.string.error_empty_minimum_order));
        requestFocusOnView(mMinimumOrderEditText);
    }

    @Override public void showEmptyGuidePriceError() {
        mGuidePriceEditText.setError(getText(R.string.error_empty_guide_price));
        requestFocusOnView(mGuidePriceEditText);
    }

    @Override public void showEmptyDescriptionError() {
        mDescriptionEditText.setError(getText(R.string.error_empty_description));
        requestFocusOnView(mDescriptionEditText);
    }

    @Override public void showEmptyLocationError() {
        mLocationEditText.setError(getText(R.string.error_empty_location));
        requestFocusOnView(mLocationEditText);
    }

    @Override public void showEmptyExpiryDateError() {
        mExpiryDateTextView.setError("");
        showSnack(R.string.error_empty_expiry_date);
        requestFocusOnView(mExpiryDateTextView);
    }

    @Override public void showEmptySizeError() {
        mSizeZEditText.setError(getText(R.string.error_empty_size));
        requestFocusOnView(mSizeZEditText);
    }

    @Override public void showEmptyCertificationError() {
        showSnack(R.string.error_empty_certification);
        requestFocusOnView(mCertificationGroupView);
    }

    @Override public void showEmptyCertificationExtraError() {
        mCertificationExtraEditText.setError(getText(R.string.error_empty_certification_extra));
        requestFocusOnView(mCertificationExtraEditText);
    }

    @Override public void showEmptyTagsError() {

    }

    private void requestFocusOnView(View view) {
        view.getParent().requestChildFocus(view, view);
        view.requestFocus();
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    @Override public void showAdvertCreated() {
        Snackbar.make(mContent, "Advert created.", Snackbar.LENGTH_LONG).show();
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void showCategoriesInView(List<Category> categories) {
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(SellingActivity.this, categories);
        mCategorySpinner.setAdapter(adapter);

        showSubcategoriesInView(categories.get(0).getSubcategories());
    }

    private void showSubcategoriesInView(List<Subcategory> subcategories) {
        SubcategorySpinnerAdapter adapter = new SubcategorySpinnerAdapter(SellingActivity.this, subcategories);
        mSubcategorySpinner.setAdapter(adapter);
    }

    @Override public void showPackagingsInView(List<Packaging> packagings) {
        PackagingSpinnerAdapter adapter = new PackagingSpinnerAdapter(SellingActivity.this, packagings);
        mPackagingSpinner.setAdapter(adapter);
    }

    @Override public void showShippingsInView(List<Shipping> shippings) {
        ShippingSpinnerAdapter adapter = new ShippingSpinnerAdapter(SellingActivity.this, shippings);
        mShippingSpinner.setAdapter(adapter);
    }

    @Override public void showConditionsInView(List<Condition> conditions) {
        ConditionSpinnerAdapter adapter = new ConditionSpinnerAdapter(SellingActivity.this, conditions);
        mConditionSpinner.setAdapter(adapter);
    }

    @Override public void showSizesInView(List<Size> sizes) {
        SizeSpinnerAdapter adapter = new SizeSpinnerAdapter(SellingActivity.this, sizes);
        mSizeSpinner.setAdapter(adapter);
    }

    @Override public void showCertificationsInView(List<Certification> certifications) {
        mCertificationGroupView.setUpCertifications(certifications);
    }

    @Override public void showPhotoInView(Photo photo) {
        mPhotoGalleryAdapter.addPhotoFile(photo);
        mPhotoGalleryRecyclerView.scrollBy(mContent.getWidth() / 3, 0);
    }

    private ProgressDialog mProgressDialog;

    @Override public void setProgressIndicator(boolean isActive) {
        if (isActive) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(SellingActivity.this);
                mProgressDialog.setMessage("Processing...");
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        } else if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @OnClick(R.id.save_and_put_on_hold_button)
    protected void onSaveButtonClick() {
        AccountManager accountManager = AccountManager.get(SellingActivity.this);
        mAccount = accountManager.getAccountsByType(getString(R.string.authenticator_account_type))[0];
        String userId = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_id));
        String username = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_name));
        String email = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_email));

        LOGD(TAG, "UserId: " + userId + "\n" + "UserName: " + username + "\n" + "Email: " + email);

        Advert advert = new Advert();
        advert.setPhotos(mPhotoGalleryAdapter.getPhotos());
        advert.setName(getAdvertTitle());
        advert.setCategoryId(getCategoryId());
        advert.setSubCategoryId(getSubcategoryId());
        advert.setPackagingId(getPackagingId());
        advert.setItemsCount(getItemCount());
        advert.setMinOrderQuantity(getMinimumOrder());
        advert.setGuidePrice(getGuidePrice());
        advert.setDescription(getDescription());
        advert.setLocation(getLocation());
        advert.setShippingId(getShippingId());
        advert.setConditionId(getConditionId());
        advert.setDateExpiresAt(getExpiryDate());
        advert.setSize(getSize());
        advert.setCertificationId(getCertificationId());
        advert.setCertificationExtra(getCertificationExtra());
        advert.setAuthorId(Integer.valueOf(userId));
        mPresenter.processAdvert(advert);
    }

    private String getAdvertTitle() {
        return mTitleEditText.getText().toString().trim();
    }

    private int getCategoryId() {
        Category category = (Category) mCategorySpinner.getSelectedItem();
        return category.getId();
    }

    private int getSubcategoryId() {
        Subcategory subcategory = (Subcategory) mSubcategorySpinner.getSelectedItem();
        return subcategory.getId();
    }

    private int getPackagingId() {
        Packaging packaging = (Packaging) mPackagingSpinner.getSelectedItem();
        return packaging.getId();
    }

    private int getItemCount() {
        String text = mItemCountEditText.getText().toString().trim();
        if (text.isEmpty()) return 0;
        return Integer.valueOf(text);
    }

    private int getMinimumOrder() {
        String text = mMinimumOrderEditText.getText().toString().trim();
        if (text.isEmpty()) return 0;
        return Integer.valueOf(text);
    }

    private String getGuidePrice() {
        return mGuidePriceEditText.getText().toString().trim();
    }

    private String getDescription() {
        return mDescriptionEditText.getText().toString().trim();
    }

    private String getLocation() {
        return mLocationEditText.getText().toString().trim();
    }

    private int getShippingId() {
        Shipping shipping = (Shipping) mShippingSpinner.getSelectedItem();
        return shipping.getId();
    }

    private int getConditionId() {
        Condition condition = (Condition) mConditionSpinner.getSelectedItem();
        return condition.getId();
    }

    private String getExpiryDate() {
        try {
            Date date = DateFormats.EXPIRY_FORMAT.parse(mExpiryDateTextView.getText().toString());
            return DateFormats.API_FORMAT.format(date);
        } catch (ParseException e) {
            LOGE(TAG, "BOOM:", e);
            return "";
        }
    }

    private String getSize() {
        String xSize = mSizeXEditText.getText().toString().trim();
        String ySize = mSizeYEditText.getText().toString().trim();
        String zSize = mSizeZEditText.getText().toString().trim();
        if (xSize.isEmpty() || ySize.isEmpty() || zSize.isEmpty()) return "";
        mSizeZEditText.setError(null);
        return String.format("%s x %s x %s", xSize, ySize, zSize);
    }

    private int getCertificationId() {
        Certification certification = mCertificationGroupView.getCertification();
        if (certification == null) return 0;
        return certification.getId();
    }

    private String getCertificationExtra() {
        return mCertificationExtraEditText.getText().toString().trim();
    }

    private String getTags() {
        return mTagsEditText.getText().toString().trim();
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
