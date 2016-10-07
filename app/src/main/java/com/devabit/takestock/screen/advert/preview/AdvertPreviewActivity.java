package com.devabit.takestock.screen.advert.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.screen.advert.adapter.AdvertPhotosAdapter;
import com.devabit.takestock.screen.main.MainActivity;
import com.devabit.takestock.utils.DateUtil;

import java.util.List;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertPreviewActivity extends AppCompatActivity implements AdvertPreviewContract.View {

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertPreviewActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    @BindView(R.id.content_product_detail) protected ViewGroup mContent;
    @BindView(R.id.guide_price_text_view) protected TextView mGuidePriceTextView;
    @BindView(R.id.minimum_order_text_view) protected TextView mMinimumOrderTextView;
    @BindView(R.id.qty_available_text_view) protected TextView mQtyAvailableTextView;
    @BindView(R.id.description_text_view) protected TextView mDescriptionTextView;
    @BindView(R.id.location_text_view) protected TextView mLocationTextView;
    @BindView(R.id.shipping_text_view) protected TextView mShippingTextView;
    @BindView(R.id.expiry_date_text_view) protected TextView mExpiryTextView;
    @BindView(R.id.certification_text_view) protected TextView mCertificationTextView;
    @BindView(R.id.condition_text_view) protected TextView mConditionTextView;
    @BindView(R.id.content_preview) protected ViewGroup mPreviewContent;

    private AdvertPreviewContract.Presenter mPresenter;

    private boolean mIsAdvertSaved;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_preview);
        ButterKnife.bind(AdvertPreviewActivity.this);
        Advert advert = getIntent().getParcelableExtra(Advert.class.getSimpleName());
        setUpToolbar(advert);
        setUpAdvertPhotos(advert.getPhotos());
        setUpAdvert(advert);
        createPresenter(advert);
    }

    private void setUpToolbar(Advert advert) {
        Toolbar toolbar = ButterKnife.findById(AdvertPreviewActivity.this, R.id.toolbar);
        toolbar.setTitle(advert.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpAdvertPhotos(List<Photo> photos) {
        RecyclerView recyclerView = ButterKnife.findById(AdvertPreviewActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AdvertPreviewActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        AdvertPhotosAdapter photosAdapter = new AdvertPhotosAdapter(AdvertPreviewActivity.this, photos);
        recyclerView.setAdapter(photosAdapter);
    }

    private void setUpAdvert(Advert advert) {
        String packaging = advert.getPackagingName();
        mGuidePriceTextView.setText(getString(R.string.advert_preview_guide_price_per_unit, advert.getGuidePrice(), packaging));
        mMinimumOrderTextView.setText(getString(R.string.advert_preview_minimum_order_unit, advert.getMinOrderQuantity(), packaging));
        mQtyAvailableTextView.setText(getString(R.string.advert_preview_qty_available_unit, advert.getItemsCount(), packaging));
        mDescriptionTextView.setText(advert.getDescription());
        mLocationTextView.setText(advert.getLocation());
        mExpiryTextView.setText(DateUtil.formatToExpiryDate(advert.getExpiresAt()));
    }

    private void createPresenter(Advert advert) {
        new AdvertPreviewPresenter(advert, Injection.provideDataRepository(AdvertPreviewActivity.this), AdvertPreviewActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertPreviewContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchAdvertRelatedData();
    }

    @Override protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override public void showShippingInView(Shipping shipping) {
        mShippingTextView.setText(shipping.getType());
    }

    @Override public void showCertificationInView(Certification certification) {
        mCertificationTextView.setText(certification.getName());
    }

    @Override public void showConditionInView(Condition condition) {
        mConditionTextView.setText(condition.getState());
    }

    @Override public void setProgressIndicator(boolean active) {
        setTouchDisabled(active);
        setContentTransparency(active);
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

    private void setContentTransparency(boolean isActive) {
        for (int i = 0; i < mPreviewContent.getChildCount(); i++) {
            View view = mPreviewContent.getChildAt(i);
            view.setAlpha(isActive ? 0.5f : 1.0f);
        }
    }

    @OnClick(R.id.make_live_button)
    protected void onMakeLiveButtonClick() {
        mPresenter.saveAdvert();
    }

    @Override public void showAdvertSaved(Advert advert) {
        Button button = ButterKnife.findById(this, R.id.make_live_button);
        button.setVisibility(View.GONE);
        showSnack(R.string.advert_preview_advert_saved);
        mIsAdvertSaved = true;
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override public void onBackPressed() {
        if (mIsAdvertSaved) {
            startActivity(MainActivity.getStartIntent(AdvertPreviewActivity.this, getString(R.string.action_advert_saved)));
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
