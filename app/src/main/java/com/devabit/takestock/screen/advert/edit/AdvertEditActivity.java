package com.devabit.takestock.screen.advert.edit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.screen.advert.adapters.*;
import com.devabit.takestock.screen.advert.dialogs.AdvertPhotoPickerDialog;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.FileUtil;
import com.devabit.takestock.utils.Logger;
import com.devabit.takestock.widgets.CertificationRadioButtonGroupView;

import java.io.File;
import java.util.List;

/**
 * Created by Victor Artemyev on 09/06/2016.
 */
public class AdvertEditActivity extends AppCompatActivity implements AdvertEditContract.View {

    private static final String TAG = Logger.makeLogTag(AdvertEditActivity.class);

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertEditActivity.class);
        starter.putExtra(Advert.class.getName(), advert);
        return starter;
    }

    private static final int REQUEST_CODE_PHOTO_LIBRARY = 101;
    private static final int REQUEST_CODE_PHOTO_CAMERA = 102;

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
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

    private Menu mMenu;
    private Advert mAdvert;

    private PhotoGalleryAdapter mPhotoGalleryAdapter;

    private AdvertEditContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_edit);
        ButterKnife.bind(AdvertEditActivity.this);
        setUpPresenter();
        setUpToolbar();
        setUpPhotosRecyclerView();
    }

    private void setUpPresenter() {
        new AdvertEditPresenter(
                Injection.provideDataRepository(AdvertEditActivity.this), AdvertEditActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertEditContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(AdvertEditActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(mMenuItemClickListener);
        toolbar.setTitle(R.string.edit_advert);
        toolbar.inflateMenu(R.menu.advert_edit_menu);
        mMenu = toolbar.getMenu();
    }

    private final Toolbar.OnMenuItemClickListener mMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_update_advert:
                    updateAdvert();
                    return true;

                default:
                    return false;
            }
        }
    };

    private void setUpPhotosRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                AdvertEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mPhotosRecyclerView.setLayoutManager(layoutManager);
        mPhotoGalleryAdapter = new PhotoGalleryAdapter(AdvertEditActivity.this);
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
        mPresenter.processPhotoUriToFile(photoUri, FileUtil.getUniquePhotoFile(AdvertEditActivity.this));
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.fetchAdvertRelatedData();
    }

    @Override public void showCategoriesInView(List<Category> categories) {
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(AdvertEditActivity.this, categories);
        mCategorySpinner.setAdapter(adapter);

        showSubcategoriesInView(categories.get(0).getSubcategories());
    }

    private void showSubcategoriesInView(List<Subcategory> subcategories) {
        SubcategorySpinnerAdapter adapter = new SubcategorySpinnerAdapter(AdvertEditActivity.this, subcategories);
        mSubcategorySpinner.setAdapter(adapter);
    }

    @Override public void showPackagingsInView(List<Packaging> packagings) {
        PackagingSpinnerAdapter adapter = new PackagingSpinnerAdapter(AdvertEditActivity.this, packagings);
        mPackagingSpinner.setAdapter(adapter);
    }

    @Override public void showShippingsInView(List<Shipping> shippings) {
        ShippingSpinnerAdapter adapter = new ShippingSpinnerAdapter(AdvertEditActivity.this, shippings);
        mShippingSpinner.setAdapter(adapter);
    }

    @Override public void showConditionsInView(List<Condition> conditions) {
        ConditionSpinnerAdapter adapter = new ConditionSpinnerAdapter(AdvertEditActivity.this, conditions);
        mConditionSpinner.setAdapter(adapter);
    }

    @Override public void showSizesInView(List<Size> sizes) {
        SizeSpinnerAdapter adapter = new SizeSpinnerAdapter(AdvertEditActivity.this, sizes);
        mSizeSpinner.setAdapter(adapter);
    }

    @Override public void showCertificationsInView(List<Certification> certifications) {
        mCertificationGroupView.setUpCertifications(certifications);
    }

    @Override public void onAdvertRelatedDataShowed() {
        mAdvert = getIntent().getParcelableExtra(Advert.class.getName());
        setUpAdvert(mAdvert);
    }

    private void setUpAdvert(Advert advert) {
        mPhotoGalleryAdapter.setPhotos(advert.getPhotos());
        mTitleEditText.setText(advert.getName());
        mItemCountEditText.setText(String.valueOf(advert.getItemsCount()));
        mMinimumOrderEditText.setText(String.valueOf(advert.getMinOrderQuantity()));
        mGuidePriceEditText.setText(advert.getGuidePrice());
        mDescriptionEditText.setText(advert.getDescription());
        mLocationEditText.setText(advert.getLocation());
        mExpiryDateTextView.setText(DateUtil.formatToExpiryDate(advert.getDateExpiresAt()));
        mCertificationGroupView.selectCertification(advert.getCertification());
        mCertificationExtraEditText.setText(advert.getCertificationExtra());
        setUpAdvertSize(advert);
    }

    private void setUpAdvertSize(Advert advert) {
        String value = advert.getSize();
        String[] xyz = value.split("x");
        mSizeXEditText.setText(xyz[0]);
        mSizeYEditText.setText(xyz[1]);
        mSizeZEditText.setText(xyz[2]);
    }

    @OnClick(R.id.expiry_date_text_view)
    protected void omExpiryTextViewClick() {
        displayDatePickerDialog();
    }

    private void displayDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(AdvertEditActivity.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mExpiryDateTextView.setError(null);
                        mExpiryDateTextView.setText(getString(R.string.expiry_date, dayOfMonth, monthOfYear, year));
                    }
                }, 1987, 27, 12);
        dialog.show();
    }

    @Override public void showEmptyPhotosError() {

    }

    @Override public void showEmptyTitleError() {

    }

    @Override public void showEmptyItemCountError() {

    }

    @Override public void showEmptyMinimumOrderError() {

    }

    @Override public void showEmptyGuidePriceError() {

    }

    @Override public void showEmptyDescriptionError() {

    }

    @Override public void showEmptyLocationError() {

    }

    @Override public void showEmptyExpiryDateError() {

    }

    @Override public void showEmptySizeError() {

    }

    @Override public void showEmptyCertificationError() {

    }

    @Override public void showEmptyCertificationExtraError() {

    }

    @Override public void showEmptyTagsError() {

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

    @Override public void showPhotoInView(Photo photo) {
        mPhotoGalleryAdapter.addPhoto(photo);
        mPhotosRecyclerView.scrollBy(mContent.getWidth() / 3, 0);
    }

    @Override public void showAdvertUpdated(Advert advert) {

    }

    private void updateAdvert() {
        Toast.makeText(AdvertEditActivity.this, "Not yet implemented.", Toast.LENGTH_LONG).show();
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
