package com.devabit.takestock.screen.advert.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.screen.advert.adapter.AdvertPhotosAdapter;
import com.devabit.takestock.screen.advert.detail.dialogs.OfferDialog;
import com.devabit.takestock.screen.askQuestion.AskQuestionActivity;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.utils.DateUtil;
import timber.log.Timber;

import java.util.List;

public class AdvertDetailActivity extends AppCompatActivity implements AdvertDetailContract.View {

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertDetailActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    private static final int RC_ENTRY = 100;

    @BindView(R.id.content_product_detail) protected View mContent;
    @BindView(R.id.guide_price_text_view) protected TextView mGuidePriceTextView;
    @BindView(R.id.minimum_order_text_view) protected TextView mMinimumOrderTextView;
    @BindView(R.id.qty_available_text_view) protected TextView mQtyAvailableTextView;
    @BindView(R.id.description_text_view) protected TextView mDescriptionTextView;
    @BindView(R.id.location_text_view) protected TextView mLocationTextView;
    @BindView(R.id.shipping_text_view) protected TextView mShippingTextView;
    @BindView(R.id.expiry_date_text_view) protected TextView mExpiryTextView;
    @BindView(R.id.certification_text_view) protected TextView mCertificationTextView;
    @BindView(R.id.condition_text_view) protected TextView mConditionTextView;

    @BindViews({R.id.recycler_view, R.id.make_offer_fab, R.id.ask_button, R.id.make_button})
    protected List<View> mViews;

    private TakeStockAccount mAccount;
    private Advert mAdvert;
    private AdvertDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);
        ButterKnife.bind(AdvertDetailActivity.this);

        mAccount = TakeStockAccount.get(AdvertDetailActivity.this);
        mAdvert = getIntent().getParcelableExtra(Advert.class.getSimpleName());
        Timber.d(mAdvert.toString());

        setUpToolbar(mAdvert);
        setUpRecyclerView(mAdvert);
        setUpAdvertDetail(mAdvert);
        createPresenter();
    }

    private void setUpToolbar(Advert advert) {
        Toolbar toolbar = ButterKnife.findById(AdvertDetailActivity.this, R.id.toolbar);
        toolbar.setTitle(advert.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpAdvertDetail(Advert advert) {
        String packaging = advert.getPackagingName();
        mGuidePriceTextView.setText(getString(R.string.advert_detail_guide_price_per_unit, advert.getGuidePrice(), packaging));
        mMinimumOrderTextView.setText(getString(R.string.advert_detail_minimum_order_unit, advert.getMinOrderQuantity(), packaging));
        mQtyAvailableTextView.setText(getString(R.string.advert_detail_qty_available_unit, advert.getItemsCount(), packaging));
        mDescriptionTextView.setText(advert.getDescription());
        mExpiryTextView.setText(DateUtil.formatToExpiryDate(advert.getExpiresAt()));
        Certification certification = advert.getCertification();
        mCertificationTextView.setText(certification == null ? "" : certification.getName());
        mLocationTextView.setText(advert.getLocation());
    }

    private void setUpRecyclerView(Advert advert) {
        RecyclerView recyclerView = ButterKnife.findById(AdvertDetailActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AdvertDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        AdvertPhotosAdapter adapter = new AdvertPhotosAdapter(AdvertDetailActivity.this, advert.getPhotos());
        recyclerView.setAdapter(adapter);
    }

    private void createPresenter() {
        new AdvertDetailPresenter(
                Injection.provideDataRepository(AdvertDetailActivity.this), AdvertDetailActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchShippingById(mAdvert.getShippingId());
        mPresenter.fetchConditionById(mAdvert.getConditionId());
    }

    @OnClick(R.id.make_offer_fab)
    protected void onMakeOfferFabClick() {
        displayOfferMakerDialog();
    }

    @OnClick(R.id.make_button)
    protected void onMakeOfferButtonClick() {
        displayOfferMakerDialog();
    }

    private void displayOfferMakerDialog() {
        if (mAccount.lacksAccount()) {
            startEntryActivity();
        } else {
            OfferDialog dialog = OfferDialog.newInstance(mAdvert);
            dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
            dialog.setOnOfferListener(new OfferDialog.OnOfferListener() {
                @Override public void onOfferMade(OfferDialog dialog, Offer offer) {
                    dialog.dismiss();
                    mPresenter.makeOffer(offer);
                }
            });
        }
    }

    private void startEntryActivity() {
        startActivityForResult(EntryActivity.getStartIntent(AdvertDetailActivity.this), RC_ENTRY);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ENTRY && resultCode == RESULT_OK) {
            Timber.d("Entry is successful.");
        }
    }

    @Override public void showShippingInView(Shipping shipping) {
        mShippingTextView.setText(shipping.getType());
    }

    @Override public void showConditionInView(Condition condition) {
        mConditionTextView.setText(condition.getState());
    }

    @Override public void showOfferMade(Offer offer) {
        showSnack(R.string.advert_detail_offer_made);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        setTouchDisabled(isActive);
        ButterKnife.apply(mViews, TRANSPARENCY, isActive);
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

    private static final ButterKnife.Setter<View, Boolean> TRANSPARENCY
            = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(@NonNull View view, Boolean isActive, int index) {
            view.setAlpha(isActive ? 0.5f : 1.0f);
        }
    };

    @OnClick(R.id.ask_button)
    protected void onAskButtonClick() {
        if (mAccount.lacksAccount()) startEntryActivity();
        else startQuestionActivity();
    }

    private void startQuestionActivity() {
        startActivity(AskQuestionActivity.getStartIntent(AdvertDetailActivity.this, mAdvert.getId()));
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
