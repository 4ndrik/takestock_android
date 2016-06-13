package com.devabit.takestock.screens.advert.create;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.screens.advert.adapters.*;
import com.devabit.takestock.screens.advert.dialogs.AdvertPhotoPickerDialog;
import com.devabit.takestock.screens.advert.preview.AdvertPreviewActivity;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.FileUtil;
import com.devabit.takestock.utils.FontCache;
import com.devabit.takestock.widgets.CertificationRadioButtonGroupView;

import java.io.File;
import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class AdvertCreateActivity extends AppCompatActivity implements AdvertCreateContract.View {

    private static final String TAG = makeLogTag(AdvertCreateActivity.class);

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AdvertCreateActivity.class);
    }

    private static final int REQUEST_CODE_PHOTO_LIBRARY = 101;
    private static final int REQUEST_CODE_PHOTO_CAMERA = 102;

    @BindView(R.id.content_activity_sell_something) protected View mContent;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;

    @BindView(R.id.recycler_view) protected RecyclerView mPhotosRecyclerView;

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

    private AdvertCreateContract.Presenter mPresenter;

    private Account mAccount;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_create);
        ButterKnife.bind(AdvertCreateActivity.this);

        new AdvertCreatePresenter(
                Injection.provideDataRepository(AdvertCreateActivity.this), AdvertCreateActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(AdvertCreateActivity.this, R.string.font_brandon_bold);
        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(@NonNull Button button, int index) {
                button.setTypeface(boldTypeface);
            }
        });
        setUpToolbar(boldTypeface);
        setUpPhotosRecyclerView();
    }

    private void setUpToolbar(Typeface typeface) {
        Toolbar toolbar = ButterKnife.findById(AdvertCreateActivity.this, R.id.toolbar);
        toolbar.setTitle(R.string.sell_something);
//        TextView title = ButterKnife.findById(toolbar, R.id.toolbar_title);
//        title.setTypeface(typeface);
//        title.setText(R.string.sell_something);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpPhotosRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                AdvertCreateActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mPhotosRecyclerView.setLayoutManager(layoutManager);
        mPhotoGalleryAdapter = new PhotoGalleryAdapter(AdvertCreateActivity.this);
        mPhotoGalleryAdapter.setOnPickPhotoListener(new PhotoGalleryAdapter.OnPickPhotoListener() {
            @Override public void onPick() {
                displayPhotoPickerDialog();
            }
        });
        mPhotosRecyclerView.setAdapter(mPhotoGalleryAdapter);
    }

    private void displayPhotoPickerDialog() {
        AdvertPhotoPickerDialog pickerDialog = AdvertPhotoPickerDialog.newInstance();
        pickerDialog.show(getSupportFragmentManager(), pickerDialog.getClass().getName());
        pickerDialog.setOnPickListener(new AdvertPhotoPickerDialog.OnPickListener() {
            @Override public void onPickFromCamera(AdvertPhotoPickerDialog dialog) {
                dialog.dismiss();
                startCameraActivity();
            }

            @Override public void onPickFromLibrary(AdvertPhotoPickerDialog dialog) {
                dialog.dismiss();
                startPhotoLibraryActivity();
            }
        });
    }

    private void startPhotoLibraryActivity() {
        Intent starter = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(starter, REQUEST_CODE_PHOTO_LIBRARY);
    }

    private Uri mPhotoUri;

    private void startCameraActivity() {
        Intent starter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoUri = Uri.fromFile(FileUtil.getPhotoFile());
        starter.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(starter, REQUEST_CODE_PHOTO_CAMERA);
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
        mPresenter.processPhotoUriToFile(photoUri, FileUtil.getUniquePhotoFile(AdvertCreateActivity.this));
    }

    @OnClick(R.id.expiry_date_text_view)
    protected void onExpiryDateTextViewClick() {
        displayDatePickerDialog();
    }

    private void displayDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(AdvertCreateActivity.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mExpiryDateTextView.setError(null);
                        mExpiryDateTextView.setText(getString(R.string.expiry_date, dayOfMonth, monthOfYear, year));
                    }
                }, 1987, 27, 12);
        dialog.show();
    }

    @Override public void setPresenter(@NonNull AdvertCreateContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.fetchAdvertRelatedData();
    }

    @Override public void showEmptyPhotosError() {
        showSnack(R.string.error_empty_photos);
        requestFocusOnView(mPhotosRecyclerView);
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

    @Override public void showAdvertInPreview(Advert advert) {
        startActivity(AdvertPreviewActivity.getStartIntent(AdvertCreateActivity.this, advert));
    }

    @Override public void showAdvertSaved(Advert advert) {
        LOGD(TAG, advert.toString());
        Snackbar.make(mContent, "Advert saved.", Snackbar.LENGTH_LONG).show();
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void showCategoriesInView(List<Category> categories) {
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(AdvertCreateActivity.this, categories);
        mCategorySpinner.setAdapter(adapter);

        showSubcategoriesInView(categories.get(0).getSubcategories());
    }

    private void showSubcategoriesInView(List<Subcategory> subcategories) {
        SubcategorySpinnerAdapter adapter = new SubcategorySpinnerAdapter(AdvertCreateActivity.this, subcategories);
        mSubcategorySpinner.setAdapter(adapter);
    }

    @Override public void showPackagingsInView(List<Packaging> packagings) {
        PackagingSpinnerAdapter adapter = new PackagingSpinnerAdapter(AdvertCreateActivity.this, packagings);
        mPackagingSpinner.setAdapter(adapter);
    }

    @Override public void showShippingsInView(List<Shipping> shippings) {
        ShippingSpinnerAdapter adapter = new ShippingSpinnerAdapter(AdvertCreateActivity.this, shippings);
        mShippingSpinner.setAdapter(adapter);
    }

    @Override public void showConditionsInView(List<Condition> conditions) {
        ConditionSpinnerAdapter adapter = new ConditionSpinnerAdapter(AdvertCreateActivity.this, conditions);
        mConditionSpinner.setAdapter(adapter);
    }

    @Override public void showSizesInView(List<Size> sizes) {
        SizeSpinnerAdapter adapter = new SizeSpinnerAdapter(AdvertCreateActivity.this, sizes);
        mSizeSpinner.setAdapter(adapter);
    }

    @Override public void showCertificationsInView(List<Certification> certifications) {
        mCertificationGroupView.setUpCertifications(certifications);
    }

    @Override public void showPhotoInView(Photo photo) {
        mPhotoGalleryAdapter.addPhoto(photo);
        mPhotosRecyclerView.scrollBy(mContent.getWidth() / 3, 0);
    }

    @Override public void setProgressIndicator(boolean isActive) {
        setProgressBarActive(isActive);
        setTouchDisabled(isActive);
        setContentInputTransparency(isActive);
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

    @OnClick(R.id.preview_ad_button)
    protected void onPreviewButton() {
        Advert advert = getAdvert();
        mPresenter.previewAdvert(advert);
    }

    @OnClick(R.id.save_and_put_on_hold_button)
    protected void onSaveButtonClick() {
        Advert advert = getAdvert();
        mPresenter.saveAdvert(advert);
    }

    private Advert getAdvert() {
        Advert advert = new Advert();
        advert.setPhotos(getPhotos());
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
        advert.setAuthorId(getUserId());
        return advert;
    }

    private List<Photo> getPhotos() {
        return mPhotoGalleryAdapter.getPhotos();
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
        String value = mExpiryDateTextView.getText().toString().trim();
        return DateUtil.formatToApiDate(value);
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

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(AdvertCreateActivity.this);
        mAccount = accountManager.getAccountsByType(getString(R.string.authenticator_account_type))[0];
        String userId = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
