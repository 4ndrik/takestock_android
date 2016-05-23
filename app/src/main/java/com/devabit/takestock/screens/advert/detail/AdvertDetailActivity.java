package com.devabit.takestock.screens.advert.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.models.Shipping;
import com.devabit.takestock.screens.advert.adapters.AdvertPhotosAdapter;
import com.devabit.takestock.util.FontCache;

import java.util.List;

import static com.devabit.takestock.util.Logger.makeLogTag;

public class AdvertDetailActivity extends AppCompatActivity implements AdvertDetailContract.View {

    private static final String TAG = makeLogTag(AdvertDetailActivity.class);

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertDetailActivity.class);
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
    @BindViews({R.id.ask_button, R.id.make_offer_button})
    protected List<Button> mButtons;

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
                        mPhotosRecyclerView.getLayoutParams().height = mContent.getHeight() / 2;
                        mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setUpAdvertPhotosToRecyclerView(mAdvert);
                    }
                });
        mGuidePriceTextView.setText(getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice()));
        mMinimumOrderTextView.setText(getString(R.string.minimum_order_kg, mAdvert.getMinOrderQuantity()));
        mQtyAvailableTextView.setText(getString(R.string.qty_available_kg, mAdvert.getItemsCount()));
        mDescriptionTextView.setText(mAdvert.getDescription());
        mLocationTextView.setText(mAdvert.getLocation());

        final Typeface boldTypeface = FontCache.getTypeface(AdvertDetailActivity.this, R.string.font_brandon_bold);
        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(@NonNull Button view, int index) {
                view.setTypeface(boldTypeface);
            }
        });

        mPresenter.fetchShippingById(mAdvert.getShippingId());
        mPresenter.fetchCertificationById(mAdvert.getCertificationId());
        mPresenter.fetchConditionById(mAdvert.getConditionId());
    }

    private void setUpAdvertPhotosToRecyclerView(Advert advert) {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(AdvertDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mPhotosRecyclerView.setLayoutManager(layoutManager);
        AdvertPhotosAdapter photosAdapter = new AdvertPhotosAdapter(AdvertDetailActivity.this, advert.getPhotos());
        mPhotosRecyclerView.setAdapter(photosAdapter);
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
