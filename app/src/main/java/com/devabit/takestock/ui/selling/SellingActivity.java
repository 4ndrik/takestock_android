package com.devabit.takestock.ui.selling;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.ui.selling.adapters.*;
import com.devabit.takestock.ui.selling.dialogs.PhotoPickerDialog;
import com.devabit.takestock.util.FontCache;
import com.devabit.takestock.widget.CertificationRadioButtonGroupView;

import java.util.List;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class SellingActivity extends AppCompatActivity implements SellingContract.View {

    private static final String TAG = makeLogTag(SellingActivity.class);

    @BindView(R.id.content_activity_selling) protected View mContent;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.expiry_text_view) protected TextView mExpiryTextView;

    @BindView(R.id.image_gallery_recycler_view) protected RecyclerView mImageGalleryRecyclerView;

    @BindView(R.id.category_spinner) protected Spinner mCategorySpinner;
    @BindView(R.id.subcategory_spinner) protected Spinner mSubcategorySpinner;
    @BindView(R.id.packaging_spinner) protected Spinner mPackagingSpinner;
    @BindView(R.id.shipping_spinner) protected Spinner mShippingSpinner;
    @BindView(R.id.condition_spinner) protected Spinner mConditionSpinner;
    @BindView(R.id.size_spinner) protected Spinner mSizeSpinner;

    @BindView(R.id.certification_group_view) protected CertificationRadioButtonGroupView mCertificationGroupView;

    @BindViews({R.id.preview_ad_button, R.id.save_and_put_on_hold_button})
    List<Button> mButtons;

    private ImageGalleryAdapter mGalleryAdapter;

    private SellingContract.Presenter mPresenter;

    private Account mAccount;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);

        AccountManager accountManager = AccountManager.get(SellingActivity.this);
        mAccount = accountManager.getAccountsByType(getString(R.string.authenticator_account_type))[0];
        String userId = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_id));
        String username = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_name));
        String email = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_email));

        LOGD(TAG, "UserId: " + userId + "\n" + "UserName: " + username + "\n" + "Email: " + email);

        final Typeface boldTypeface = FontCache.getTypeface(SellingActivity.this, R.string.font_brandon_bold);
        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(@NonNull Button button, int index) {
                button.setTypeface(boldTypeface);
            }
        });
        setUpToolbar(boldTypeface);
        setUpImageGalleryRecyclerView();

        new SellingPresenter(
                Injection.provideDataRepository(SellingActivity.this), SellingActivity.this);
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
        mImageGalleryRecyclerView.setLayoutManager(layoutManager);
        mGalleryAdapter = new ImageGalleryAdapter(SellingActivity.this);
        mGalleryAdapter.setOnPickPhotoListener(new ImageGalleryAdapter.OnPickPhotoListener() {
            @Override public void onPick() {
//                mGalleryAdapter.addImagePath("http://livelovefruit.com/wp-content/uploads/2015/06/Benefits-of-eating-fruit-forbreakfast.jpg");
//                mImageGalleryRecyclerView.scrollBy(mContent.getWidth() / 3, 0);
                displayPhotoPickerDialog();
            }
        });
        mImageGalleryRecyclerView.setAdapter(mGalleryAdapter);
    }

    private void displayPhotoPickerDialog() {
        PhotoPickerDialog dialog = PhotoPickerDialog.newInstance();
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
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

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
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
