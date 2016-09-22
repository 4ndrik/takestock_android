package com.devabit.takestock.screen.advert.create;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.screen.advert.adapter.*;
import com.devabit.takestock.screen.advert.dialog.AdvertPhotoPickerDialog;
import com.devabit.takestock.screen.advert.dialog.KeywordDialog;
import com.devabit.takestock.screen.advert.preview.AdvertPreviewActivity;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.ImagePickerUtil;
import com.devabit.takestock.widget.CertificationRadioButtonGroupView;
import com.devabit.takestock.widget.FlexboxLayout;
import com.devabit.takestock.widget.HintSpinnerAdapter;
import timber.log.Timber;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.devabit.takestock.R.id.toolbar;
import static com.devabit.takestock.utils.PermissionChecker.*;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class AdvertCreateActivity extends AppCompatActivity implements AdvertCreateContract.View {

    private static final String EXTRA_ADVERT = "ADVERT";

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = getStartIntent(context);
        starter.putExtra(EXTRA_ADVERT, advert);
        return starter;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AdvertCreateActivity.class);
    }

    private static final int RC_CAMERA_PERMISSION = 103;
    private static final int RC_GALLERY_PERMISSION = 104;

    @BindView(R.id.content) protected View mContent;
    @BindView(toolbar) protected Toolbar mToolbar;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;

    @BindView(R.id.recycler_view) protected RecyclerView mPhotosRecyclerView;

    @BindView(R.id.category_spinner) protected Spinner mCategorySpinner;
    @BindView(R.id.subcategory_spinner) protected Spinner mSubcategorySpinner;
    @BindView(R.id.packaging_spinner) protected Spinner mPackagingSpinner;
    @BindView(R.id.shipping_spinner) protected Spinner mShippingSpinner;
    @BindView(R.id.condition_spinner) protected Spinner mConditionSpinner;
    @BindView(R.id.size_spinner) protected Spinner mSizeSpinner;

    @BindView(R.id.subcategory_text_view) protected TextView mSubcategoryTextView;
    @BindView(R.id.sale_packaging_text_view) protected TextView mSalePackagingTextView;
    @BindView(R.id.order_packaging_text_view) protected TextView mOrderPackagingTextView;
    @BindView(R.id.price_packaging_text_view) protected TextView mPricePackagingTextView;

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
    @BindView(R.id.keywords_flexbox_layout) protected FlexboxLayout mKeywordsFlexboxLayout;
    @BindView(R.id.expiry_date_text_view) protected TextView mExpiryDateTextView;
    @BindView(R.id.certification_group_view) protected CertificationRadioButtonGroupView mCertificationGroupView;

    private PhotoGalleryAdapter mPhotoGalleryAdapter;

    private AdvertCreateContract.Presenter mPresenter;
    private Advert mAdvert;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_create);
        ButterKnife.bind(AdvertCreateActivity.this);
        mAdvert = getIntent().getParcelableExtra(EXTRA_ADVERT);
        setUpToolbar();
        setUpRecyclerView();
        createPresenter();
    }

    private void setUpToolbar() {
        mToolbar.setTitle(mAdvert == null
                ? R.string.advert_create_toolbar_title_create
                : R.string.advert_create_toolbar_title_edit);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView() {
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

    private void createPresenter() {
        new AdvertCreatePresenter(
                Injection.provideDataRepository(AdvertCreateActivity.this), AdvertCreateActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertCreateContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchAdvertRelatedData();
    }

    private void displayPhotoPickerDialog() {
        AdvertPhotoPickerDialog pickerDialog = AdvertPhotoPickerDialog.newInstance();
        pickerDialog.show(getSupportFragmentManager(), pickerDialog.getClass().getName());
        pickerDialog.setOnPickListener(new AdvertPhotoPickerDialog.OnPickListener() {
            @Override public void onPickFromCamera(AdvertPhotoPickerDialog dialog) {
                dialog.dismiss();
                pickPhotoFromCamera();
            }

            @Override public void onPickFromStorage(AdvertPhotoPickerDialog dialog) {
                dialog.dismiss();
                pickPhotoFromStorage();
            }
        });
    }

    private void pickPhotoFromCamera() {
        if (lacksPermissions(AdvertCreateActivity.this, CAMERA_PERMISSIONS)) {
            ActivityCompat.requestPermissions(AdvertCreateActivity.this,
                    CAMERA_PERMISSIONS, RC_CAMERA_PERMISSION);
        } else {
            ImagePickerUtil.openCamera(AdvertCreateActivity.this);
        }
    }

    private void pickPhotoFromStorage() {
        if (lacksPermissions(AdvertCreateActivity.this, STORAGE_PERMISSIONS)) {
            ActivityCompat.requestPermissions(AdvertCreateActivity.this,
                    STORAGE_PERMISSIONS, RC_GALLERY_PERMISSION);
        } else {
            ImagePickerUtil.openGallery(AdvertCreateActivity.this);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!grantPermissions(grantResults)) return;
        switch (requestCode) {
            case RC_CAMERA_PERMISSION:
                ImagePickerUtil.openCamera(AdvertCreateActivity.this);
                break;
            case RC_GALLERY_PERMISSION:
                ImagePickerUtil.openGallery(AdvertCreateActivity.this);
                break;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePickerUtil.handleActivityResult(requestCode, resultCode, data, AdvertCreateActivity.this,
                new ImagePickerUtil.OnImagePickedListener() {
                    @Override public void onImagePicked(File imageFile, String source) {
                        Timber.d("onImagePicked: %s, %s", imageFile.toString(), source);
                        setUpImage(imageFile);
                    }

                    @Override public void onCanceled(String source) {
                        Timber.d("onCanceled: %s", source);
                    }

                    @Override public void onError(Throwable throwable, String source) {
                        Timber.e(throwable, "onError");
                    }
                });
    }

    private void setUpImage(File imageFile) {
        Photo photo = new Photo.Builder()
                .setImage(imageFile.getAbsolutePath())
                .build();
        mPhotoGalleryAdapter.addPhoto(photo);
        mPhotosRecyclerView.scrollBy(mContent.getWidth() / 3, 0);
    }

    @OnClick(R.id.expiry_date_text_view)
    protected void onExpiryDateTextViewClick() {
        displayDatePickerDialog();
    }

    private void displayDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(AdvertCreateActivity.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mExpiryDateTextView.setError(null);
                        mExpiryDateTextView.setText(getString(R.string.advert_create_expiry_date, dayOfMonth, monthOfYear + 1, year));
                    }
                }, year, month, day);
        dialog.show();
    }

    @Override public void showCategoriesInView(final List<Category> categories) {
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(AdvertCreateActivity.this, categories);
        final HintSpinnerAdapter<Category> hintAdapter = new HintSpinnerAdapter<>(
                adapter, R.layout.item_spinner, R.string.advert_create_select_one, AdvertCreateActivity.this);
        mCategorySpinner.setAdapter(hintAdapter);
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = hintAdapter.getItem(position);
                showSubcategoriesInView(category);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showSubcategoriesInView(Category category) {
        if (category == null || category.getSubcategories().isEmpty()) {
            mSubcategorySpinner.setAdapter(null);
            setSubcategoryContentVisibility(false);
        } else {
            SubcategorySpinnerAdapter adapter = new SubcategorySpinnerAdapter(
                    AdvertCreateActivity.this, category.getSubcategories());
            HintSpinnerAdapter<Subcategory> nothingSelectedAdapter = new HintSpinnerAdapter<>(
                    adapter, R.layout.item_spinner, R.string.advert_create_select_one, AdvertCreateActivity.this);
            mSubcategorySpinner.setAdapter(nothingSelectedAdapter);
            setSubcategoryContentVisibility(true);
        }
    }

    private void setSubcategoryContentVisibility(boolean visible) {
        mSubcategoryTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mSubcategorySpinner.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override public void showPackagingsInView(List<Packaging> packagings) {
        final PackagingSpinnerAdapter adapter = new PackagingSpinnerAdapter(AdvertCreateActivity.this, packagings);
        mPackagingSpinner.setAdapter(adapter);
        mPackagingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Packaging packaging = adapter.getItem(position);
                setUpPackagingInViews(packaging);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpPackagingInViews(Packaging packaging) {
        String type = packaging.getType();
        mSalePackagingTextView.setText(type);
        mOrderPackagingTextView.setText(type);
        mPricePackagingTextView.setText(getString(R.string.per_packaging, type));
    }

    @Override public void showShippingsInView(List<Shipping> shippings) {
        ShippingSpinnerAdapter adapter = new ShippingSpinnerAdapter(AdvertCreateActivity.this, shippings);
        HintSpinnerAdapter<Shipping> hintAdapter = new HintSpinnerAdapter<>(
                adapter, R.layout.item_spinner, R.string.advert_create_select_one, AdvertCreateActivity.this);
        mShippingSpinner.setAdapter(hintAdapter);
    }

    @Override public void showConditionsInView(List<Condition> conditions) {
        ConditionSpinnerAdapter adapter = new ConditionSpinnerAdapter(AdvertCreateActivity.this, conditions);
        HintSpinnerAdapter<Condition> hintAdapter = new HintSpinnerAdapter<>(
                adapter, R.layout.item_spinner, R.string.advert_create_select_one, AdvertCreateActivity.this);
        mConditionSpinner.setAdapter(hintAdapter);
    }

    @Override public void showSizesInView(List<Size> sizes) {
        SizeSpinnerAdapter adapter = new SizeSpinnerAdapter(AdvertCreateActivity.this, sizes);
        mSizeSpinner.setAdapter(adapter);
    }

    @Override public void showCertificationsInView(List<Certification> certifications) {
        mCertificationGroupView.setUpCertifications(certifications);
    }

    @Override public void showAdvertRelatedDataFetched() {
        setUpAdvert();
    }

    private void setUpAdvert() {
        if (mAdvert == null) return;
        mPhotoGalleryAdapter.setPhotos(mAdvert.getPhotos());
        mTitleEditText.setText(mAdvert.getName());
        mItemCountEditText.setText(String.valueOf(mAdvert.getItemsCount()));
        mMinimumOrderEditText.setText(String.valueOf(mAdvert.getMinOrderQuantity()));
        mGuidePriceEditText.setText(mAdvert.getGuidePrice());
        mDescriptionEditText.setText(mAdvert.getDescription());
        mLocationEditText.setText(mAdvert.getLocation());
        mExpiryDateTextView.setText(DateUtil.formatToExpiryDate(mAdvert.getExpiresAt()));
        mCertificationGroupView.selectCertification(mAdvert.getCertification());
        mCertificationExtraEditText.setText(mAdvert.getCertificationExtra());
        String value = mAdvert.getSize();
        if (!value.isEmpty()) {
            String[] xyz = value.split("x");
            mSizeXEditText.setText(xyz[0]);
            mSizeYEditText.setText(xyz[1]);
            mSizeZEditText.setText(xyz[2]);
        }
    }

    @Override public void showEmptyPhotosError() {
        showSnack(R.string.advert_create_error_photo);
        requestFocusOnView(mPhotosRecyclerView);
    }

    @Override public void showEmptyTitleError() {
        mTitleEditText.setError(getText(R.string.advert_create_error_title));
        requestFocusOnView(mTitleEditText);
    }

    @Override public void showEmptyCategoryError() {
        showSnack(R.string.error_category);
        requestFocusOnView(mCategorySpinner);
    }

    @Override public void showEmptyItemCountError() {
        mItemCountEditText.setError(getText(R.string.advert_create_error_item_count));
        requestFocusOnView(mItemCountEditText);
    }

    @Override public void showEmptyMinimumOrderError() {
        mMinimumOrderEditText.setError(getText(R.string.advert_create_error_minimum_order));
        requestFocusOnView(mMinimumOrderEditText);
    }

    @Override public void showEmptyGuidePriceError() {
        mGuidePriceEditText.setError(getText(R.string.advert_create_error_guide_price));
        requestFocusOnView(mGuidePriceEditText);
    }

    @Override public void showEmptyDescriptionError() {
        mDescriptionEditText.setError(getText(R.string.advert_create_error_description));
        requestFocusOnView(mDescriptionEditText);
    }

    @Override public void showEmptyLocationError() {
        mLocationEditText.setError(getText(R.string.advert_create_error_location));
        requestFocusOnView(mLocationEditText);
    }

    @Override public void showEmptyShippingError() {
        showSnack(R.string.error_shipping);
        requestFocusOnView(mShippingSpinner);
    }

    @Override public void showEmptyConditionError() {
        showSnack(R.string.error_condition);
        requestFocusOnView(mConditionSpinner);
    }

    @Override public void showEmptyExpiryDateError() {
        mExpiryDateTextView.setError("");
        showSnack(R.string.advert_create_error_expiry_date);
        requestFocusOnView(mExpiryDateTextView);
    }

    @Override public void showEmptySizeError() {
        mSizeZEditText.setError(getText(R.string.error_empty_size));
        requestFocusOnView(mSizeZEditText);
    }

    @Override public void showEmptyCertificationError() {
        showSnack(R.string.advert_create_error_certification);
        requestFocusOnView(mCertificationGroupView);
    }

    @Override public void showEmptyCertificationExtraError() {
        mCertificationExtraEditText.setError(getText(R.string.advert_create_error_certification_extra));
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

    @Override public void showSavedAdvert(Advert advert) {
        showSnack(R.string.advert_create_advert_saved);
        mAdvert = advert;
        mToolbar.setTitle(R.string.advert_create_toolbar_title_edit);
    }

    @Override public void showPreviewedAdvert(Advert advert) {
        startActivity(AdvertPreviewActivity.getStartIntent(AdvertCreateActivity.this, advert));
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
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

    @OnClick(R.id.add_word_text_view)
    protected void onAddKeywordTextViewClick() {
        displayKeywordDialog();
    }

    private void displayKeywordDialog() {
        KeywordDialog dialog = KeywordDialog.newInstance();
        dialog.setOnKeywordListener(new KeywordDialog.OnKeywordListener() {
            @Override public void onAdd(KeywordDialog dialog, String word) {
                dialog.dismiss();
                addKeyword(word);
            }
        });
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void addKeyword(String word) {
        TextView textView = inflateKeywordTextView();
        textView.setText(word);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mKeywordsFlexboxLayout.removeView(v);
            }
        });
        mKeywordsFlexboxLayout.addView(textView);
    }

    private TextView inflateKeywordTextView() {
        return (TextView) getLayoutInflater().inflate(R.layout.item_keyword, mKeywordsFlexboxLayout, false);
    }

    @OnClick(R.id.save_button)
    protected void onSaveButtonClick() {
        mPresenter.saveAdvert(createAdvert());
    }

    @OnClick(R.id.preview_button)
    protected void onPreviewButton() {
        mPresenter.previewAdvert(createAdvert());
    }

    private Advert createAdvert() {
        return new Advert.Builder()
                .setPhotos(getPhotos())
                .setName(getAdvertTitle())
                .setCategoryId(getCategoryId())
                .setSubcategoryId(getSubcategoryId())
                .setPackagingId(getPackagingId())
                .setPackagingName(getPackagingName())
                .setItemsCount(getItemCount())
                .setMinOrderQuantity(getMinimumOrder())
                .setGuidePrice(getGuidePrice())
                .setDescription(getDescription())
                .setLocation(getLocation())
                .setShippingId(getShippingId())
                .setConditionId(getConditionId())
                .setExpiresAt(getExpiryDate())
                .setSize(getSize())
                .setCertificationId(getCertificationId())
                .setCertificationExtra(getCertificationExtra())
                .setTags(getKeywords())
                .setAuthorId(getUserId())
                .create();
    }

    private List<Photo> getPhotos() {
        return mPhotoGalleryAdapter.getPhotos();
    }

    private String getAdvertTitle() {
        return mTitleEditText.getText().toString().trim();
    }

    private int getCategoryId() {
        Category category = (Category) mCategorySpinner.getSelectedItem();
        return category == null ? -1 : category.getId();
    }

    private int getSubcategoryId() {
        Subcategory subcategory = (Subcategory) mSubcategorySpinner.getSelectedItem();
        return subcategory == null ? -1 : subcategory.getId();
    }

    private int getPackagingId() {
        Packaging packaging = (Packaging) mPackagingSpinner.getSelectedItem();
        return packaging == null ? -1 : packaging.getId();
    }

    private String getPackagingName() {
        Packaging packaging = (Packaging) mPackagingSpinner.getSelectedItem();
        return packaging == null ? "" : packaging.getType();
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
        return shipping == null ? -1 : shipping.getId();
    }

    private int getConditionId() {
        Condition condition = (Condition) mConditionSpinner.getSelectedItem();
        return condition == null ? -1 : condition.getId();
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
        return certification == null ? -1 : certification.getId();
    }

    private String getCertificationExtra() {
        return mCertificationExtraEditText.getText().toString().trim();
    }

    private List<String> getKeywords() {
        int wordsCount = mKeywordsFlexboxLayout.getChildCount();
        List<String> result = new ArrayList<>(wordsCount);
        for (int position = 0; position < wordsCount; position++) {
            TextView textView = (TextView) mKeywordsFlexboxLayout.getChildAt(position);
            result.add(textView.getText().toString());
        }
        return result;
    }

    private int getUserId() {
        return TakeStockAccount.get(AdvertCreateActivity.this).getUserId();
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
