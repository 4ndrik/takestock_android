package com.devabit.takestock.screens.advert.detail;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.screens.advert.adapters.AdvertPhotosAdapter;
import com.devabit.takestock.screens.advert.detail.dialogs.OfferMakerDialog;
import com.devabit.takestock.screens.entry.EntryActivity;
import com.devabit.takestock.screens.questions.QuestionsActivity;
import com.devabit.takestock.util.FontCache;

import java.util.List;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

public class AdvertDetailActivity extends AppCompatActivity implements AdvertDetailContract.View {

    private static final String TAG = makeLogTag(AdvertDetailActivity.class);

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertDetailActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    private static final int REQUEST_CODE_LACK_ACCOUNT = 100;

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

    @BindViews({R.id.advert_photos_recycler_view, R.id.make_offer_fab, R.id.ask_button, R.id.make_offer_button})
    protected List<View> mViews;

    private Account mAccount;
    private Advert mAdvert;
    private AdvertDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);
        ButterKnife.bind(AdvertDetailActivity.this);

        new AdvertDetailPresenter(
                Injection.provideDataRepository(AdvertDetailActivity.this), AdvertDetailActivity.this);

        mAccount = getAccountOrNull();
        mAdvert = getIntent().getParcelableExtra(Advert.class.getSimpleName());

        Toolbar toolbar = ButterKnife.findById(AdvertDetailActivity.this, R.id.toolbar);
        toolbar.setTitle(mAdvert.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        mGuidePriceTextView.setText(getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice()));
        mMinimumOrderTextView.setText(getString(R.string.minimum_order_kg, mAdvert.getMinOrderQuantity()));
        mQtyAvailableTextView.setText(getString(R.string.qty_available_kg, mAdvert.getItemsCount()));
        mDescriptionTextView.setText(mAdvert.getDescription());
        mLocationTextView.setText(mAdvert.getLocation());
        setPhotosToRecyclerView(mAdvert.getPhotos());
        Typeface boldTypeface = FontCache.getTypeface(AdvertDetailActivity.this, R.string.font_brandon_bold);
        setTypefaceToButtons(boldTypeface);

        mPresenter.fetchShippingById(mAdvert.getShippingId());
        mPresenter.fetchCertificationById(mAdvert.getCertificationId());
        mPresenter.fetchConditionById(mAdvert.getConditionId());
    }

    @Nullable private Account getAccountOrNull() {
        AccountManager accountManager = AccountManager.get(AdvertDetailActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        if (accounts.length == 0) return null;
        else return accounts[0];
    }

    private void setPhotosToRecyclerView(final List<Photo> photos) {
        final RecyclerView recyclerView = ButterKnife.findById(
                AdvertDetailActivity.this, R.id.advert_photos_recycler_view);
        mContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        recyclerView.getLayoutParams().height = mContent.getHeight() / 2;
                        mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        LinearLayoutManager layoutManager =
                                new LinearLayoutManager(AdvertDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        AdvertPhotosAdapter photosAdapter = new AdvertPhotosAdapter(AdvertDetailActivity.this, photos);
                        recyclerView.setAdapter(photosAdapter);
                    }
                });
    }

    private void setTypefaceToButtons(Typeface typeface) {
        Button askButton = ButterKnife.findById(AdvertDetailActivity.this, R.id.ask_button);
        askButton.setTypeface(typeface);
        Button makeOfferButton = ButterKnife.findById(AdvertDetailActivity.this, R.id.make_offer_button);
        makeOfferButton.setTypeface(typeface);
    }

    @OnClick(R.id.make_offer_fab)
    protected void onMakeOfferFabClick() {
        displayOfferMakerDialog();
    }

    @OnClick(R.id.make_offer_button)
    protected void onMakeOfferButtonClick() {
        displayOfferMakerDialog();
    }

    private void displayOfferMakerDialog() {
        if (lacksAccount()) startEntryActivity();
        OfferMakerDialog dialog = OfferMakerDialog.newInstance(getUserId(), mAdvert);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnOfferMadeListener(new OfferMakerDialog.OnOfferMadeListener() {
            @Override public void onOfferMade(OfferMakerDialog dialog, Offer offer) {
                dialog.dismiss();
                mPresenter.makeOffer(offer);
            }
        });
    }

    private boolean lacksAccount() {
        return mAccount == null;
    }

    private void startEntryActivity() {
        startActivityForResult(
                EntryActivity.getStartIntent(AdvertDetailActivity.this), REQUEST_CODE_LACK_ACCOUNT);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LACK_ACCOUNT && resultCode == RESULT_OK) {
            mAccount = getAccountOrNull();
        }
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(AdvertDetailActivity.this);
        String userId = accountManager.getUserData(mAccount, getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
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

    @Override public void showOfferMade(Offer offer) {
        LOGD(TAG, "Offer made: " + offer);
        Snackbar.make(mContent, "Offer made.", Snackbar.LENGTH_LONG).show();
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
        if (lacksAccount()) startEntryActivity();
        else startQuestionActivity();
    }

    private void startQuestionActivity() {
        startActivity(QuestionsActivity.getStartIntent(AdvertDetailActivity.this, mAdvert.getId()));
    }

    @Override public void setPresenter(@NonNull AdvertDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }
}
