package com.devabit.takestock.ui.selling;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.ui.selling.adapter.*;
import com.devabit.takestock.util.FontCache;
import com.devabit.takestock.widget.CertificationRadioButtonGroupView;

import java.util.List;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class SellingActivity extends AppCompatActivity implements SellingContract.View {

    @BindView(R.id.content_activity_selling) protected View mContent;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.add_image_product_button) protected Button mAddImageButton;
    @BindView(R.id.expiry_text_view) protected TextView mExpiryTextView;

    @BindView(R.id.category_spinner) protected Spinner mCategorySpinner;
    @BindView(R.id.subcategory_spinner) protected Spinner mSubcategorySpinner;
    @BindView(R.id.shipping_spinner) protected Spinner mShippingSpinner;
    @BindView(R.id.condition_spinner) protected Spinner mConditionSpinner;
    @BindView(R.id.size_spinner) protected Spinner mSizeSpinner;

    @BindView(R.id.certification_group_view) protected CertificationRadioButtonGroupView mCertificationGroupView;

    private SellingContract.Presenter mPresenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);
        setUpToolbar();

        Typeface boldTypeface = FontCache.getTypeface(SellingActivity.this, R.string.font_brandon_bold);
        mAddImageButton.setTypeface(boldTypeface);

        new SellingPresenter(
                Injection.provideDataRepository(SellingActivity.this), SellingActivity.this);
        mPresenter.create();
    }

    private void setUpToolbar() {
        mToolbar.inflateMenu(R.menu.main);
        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        title.setTypeface(boldTypeface);
        title.setText(R.string.sell_something);
        TextView homeTextView = (TextView) mToolbar.findViewById(R.id.toolbar_back);
        Typeface mediumTypeface = FontCache.getTypeface(this, R.string.font_brandon_medium);
        homeTextView.setTypeface(mediumTypeface);
        homeTextView.setText(R.string.home);
        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override public void setPresenter(SellingContract.Presenter presenter) {
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
