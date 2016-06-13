package com.devabit.takestock.screens.advert.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.models.Shipping;
import com.devabit.takestock.screens.advert.adapters.AdvertPhotosAdapter;
import com.devabit.takestock.screens.advert.detail.AdvertDetailActivity;

import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertPreviewActivity extends AppCompatActivity implements AdvertPreviewContract.View {

    private static final String TAG = makeLogTag(AdvertDetailActivity.class);

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertPreviewActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    @BindView(R.id.content_product_detail) protected View mContent;
    @BindView(R.id.advert_photos_recycler_view) protected RecyclerView mPhotosRecyclerView;
    @BindView(R.id.guide_price_text_view) protected TextView mGuidePriceTextView;
    @BindView(R.id.minimum_order_text_view) protected TextView mMinimumOrderTextView;
    @BindView(R.id.qty_available_text_view) protected TextView mQtyAvailableTextView;
    @BindView(R.id.description_text_view) protected TextView mDescriptionTextView;
    @BindView(R.id.location_text_view) protected TextView mLocationTextView;
    @BindView(R.id.shipping_text_view) protected TextView mShippingTextView;
    @BindView(R.id.expiry_date_text_view) protected TextView mExpiryTextView;
    @BindView(R.id.certification_text_view) protected TextView mCertificationTextView;
    @BindView(R.id.condition_text_view) protected TextView mConditionTextView;

    private AdvertPreviewContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_preview);
        ButterKnife.bind(AdvertPreviewActivity.this);
        new AdvertPreviewPresenter(
                Injection.provideDataRepository(AdvertPreviewActivity.this), AdvertPreviewActivity.this);

        final Advert advert = getIntent().getParcelableExtra(Advert.class.getSimpleName());

        Toolbar toolbar = ButterKnife.findById(AdvertPreviewActivity.this, R.id.toolbar);
        toolbar.setTitle(advert.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        mContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        mPhotosRecyclerView.getLayoutParams().height = mContent.getHeight() / 2;
                        mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setUpAdvertPhotosToRecyclerView(advert);
                    }
                });
        mGuidePriceTextView.setText(getString(R.string.guide_price_per_kg, advert.getGuidePrice()));
        mMinimumOrderTextView.setText(getString(R.string.minimum_order_kg, advert.getMinOrderQuantity()));
        mQtyAvailableTextView.setText(getString(R.string.qty_available_kg, advert.getItemsCount()));
        mDescriptionTextView.setText(advert.getDescription());
        mLocationTextView.setText(advert.getLocation());

        mPresenter.fetchShippingById(advert.getShippingId());
        mPresenter.fetchCertificationById(advert.getCertificationId());
        mPresenter.fetchConditionById(advert.getConditionId());
    }

    private void setUpAdvertPhotosToRecyclerView(Advert advert) {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(AdvertPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mPhotosRecyclerView.setLayoutManager(layoutManager);
        AdvertPhotosAdapter photosAdapter = new AdvertPhotosAdapter(AdvertPreviewActivity.this, advert.getPhotos());
        mPhotosRecyclerView.setAdapter(photosAdapter);
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

    @Override public void setPresenter(@NonNull AdvertPreviewContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
