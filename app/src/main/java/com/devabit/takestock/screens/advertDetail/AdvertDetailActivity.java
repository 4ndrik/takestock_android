package com.devabit.takestock.screens.advertDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.models.Shipping;
import com.squareup.picasso.Picasso;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

public class AdvertDetailActivity extends AppCompatActivity implements AdvertDetailContract.View {

    private static final String TAG = makeLogTag(AdvertDetailActivity.class);

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertDetailActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    @BindView(R.id.content_product_detail) protected View mContent;
    @BindView(R.id.product_image_view) protected ImageView mProductImageView;
    @BindView(R.id.guide_price_text_view) protected TextView mGuidePriceTextView;
    @BindView(R.id.minimum_order_text_view) protected TextView mMinimumOrderTextView;
    @BindView(R.id.qty_available_text_view) protected TextView mQtyAvailableTextView;
    @BindView(R.id.description_text_view) protected TextView mDescriptionTextView;
    @BindView(R.id.location_text_view) protected TextView mLocationTextView;
    @BindView(R.id.shipping_text_view) protected TextView mShippingTextView;
    @BindView(R.id.expiry_date_text_view) protected TextView mExpiryTextView;
    @BindView(R.id.certification_text_view) protected TextView mCertificationTextView;
    @BindView(R.id.condition_text_view) protected TextView mConditionTextView;

    private Advert mAdvert;
    private AdvertDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(AdvertDetailActivity.this);

        new AdvertDetailPresenter(
                Injection.provideDataRepository(AdvertDetailActivity.this), AdvertDetailActivity.this);

        mAdvert = getIntent().getParcelableExtra(Advert.class.getSimpleName());
        LOGD(TAG, "Advert: " + mAdvert);

        Toolbar toolbar = ButterKnife.findById(AdvertDetailActivity.this, R.id.toolbar);
        toolbar.setTitle(mAdvert.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        mContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        mProductImageView.getLayoutParams().height = mContent.getHeight() / 2;
                        mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        loadPhoto();
                    }
                });
        mGuidePriceTextView.setText(getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice()));
        mMinimumOrderTextView.setText(getString(R.string.minimum_order_kg, mAdvert.getMinOrderQuantity()));
        mQtyAvailableTextView.setText(getString(R.string.qty_available_kg, mAdvert.getItemsCount()));
        mDescriptionTextView.setText(mAdvert.getDescription());
        mLocationTextView.setText(mAdvert.getLocation());

        mPresenter.fetchShippingById(mAdvert.getShippingId());
        mPresenter.fetchCertificationById(mAdvert.getCertificationId());
        mPresenter.fetchConditionById(mAdvert.getConditionId());
    }

    private void loadPhoto() {
        if (mAdvert.getPhotos().isEmpty()) {
            mProductImageView.setImageResource(R.drawable.ic_image_48dp);
        } else {
            String url = mAdvert.getPhotos().get(0).getImagePath();
            Picasso.with(AdvertDetailActivity.this)
                    .load(url)
                    .placeholder(R.drawable.ic_image_48dp)
                    .error(R.drawable.ic_image_48dp)
                    .into(mProductImageView);
        }
    }

    @OnClick(R.id.make_offer_fab)
    protected void onMakeOfferFabClick() {
        OfferDialog dialog = OfferDialog.newInstance();
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
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

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showUnknownError() {

    }

    @Override public void setProgressIndicator(boolean isActive) {

    }

    @Override public void setPresenter(@NonNull AdvertDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }
}
