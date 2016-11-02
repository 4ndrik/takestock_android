package com.devabit.takestock.screen.shipping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.*;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 05/10/2016.
 */

public class ShippingActivity extends AppCompatActivity implements ShippingContract.View {

    private static final String EXTRA_OFFER = "EXTRA_OFFER";

    public static Intent getStartIntent(Context context, Offer offer) {
        Intent starter = new Intent(context, ShippingActivity.class);
        starter.putExtra(EXTRA_OFFER, offer);
        return starter;
    }

    @BindView(R.id.activity_shipping_content) ViewGroup mContent;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.house_input_layout) TextInputLayout mHouseInputLayout;
    @BindView(R.id.street_input_layout) TextInputLayout mStreetInputLayout;
    @BindView(R.id.city_input_layout) TextInputLayout mCityInputLayout;
    @BindView(R.id.postcode_input_layout) TextInputLayout mPostcodeInputLayout;
    @BindView(R.id.phone_input_layout) TextInputLayout mPhoneInputLayout;
    @BindView(R.id.house_edit_text) TextInputEditText mHouseEditText;
    @BindView(R.id.street_edit_text) TextInputEditText mStreetEditText;
    @BindView(R.id.city_edit_text) TextInputEditText mCityEditText;
    @BindView(R.id.postcode_edit_text) TextInputEditText mPostcodeEditText;
    @BindView(R.id.phone_edit_text) TextInputEditText mPhoneEditText;

    Offer mOffer;
    ShippingContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        ButterKnife.bind(ShippingActivity.this);
        mOffer = getIntent().getParcelableExtra(EXTRA_OFFER);
        setUpToolbar();
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(ShippingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createPresenter() {
        new ShippingPresenter(Injection.provideDataRepository(ShippingActivity.this), ShippingActivity.this);
    }

    @Override public void setPresenter(@NonNull ShippingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnTextChanged(R.id.house_edit_text)
    void onHouseTextChanged() {
        if (mHouseInputLayout.isErrorEnabled()) {
            mHouseInputLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.street_edit_text)
    void onStreetTextChanged() {
        if (mStreetInputLayout.isErrorEnabled()) {
            mStreetInputLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.city_edit_text)
    void onCityTextChanged() {
        if (mCityInputLayout.isErrorEnabled()) {
            mCityInputLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.postcode_edit_text)
    void onPostcodeTextChanged() {
        if (mPostcodeInputLayout.isErrorEnabled()) {
            mPostcodeInputLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.phone_edit_text)
    void onPhoneTextChanged() {
        if (mPhoneInputLayout.isErrorEnabled()) {
            mPhoneInputLayout.setErrorEnabled(false);
        }
    }

    @OnFocusChange(R.id.phone_edit_text)
    protected void onPhoneNumberEditTextFocusChanged(EditText editText, boolean focusable) {
        if (!focusable && editText.getText().length() == 0) {
            mPhoneInputLayout.setHint(getString(R.string.shipping_activity_hint_phone_template));
        } else {
            mPhoneInputLayout.setHint(getString(R.string.shipping_activity_hint_phone_number));
        }
    }

    @OnEditorAction(R.id.phone_edit_text)
    boolean onPhoneEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mPresenter.acceptOffer(mOffer, createOfferAccept());
            return true;
        }
        return false;
    }

    @OnClick(R.id.ok_button)
    void onOkButtonClick() {
        mPresenter.acceptOffer(mOffer, createOfferAccept());
    }

    private Offer.Accept createOfferAccept() {
        return new Offer.Accept.Builder()
                .setOfferId(mOffer.getId())
                .setStatus(Offer.Status.ADDRESS_RECEIVED)
                .setFromSeller(false)
                .setHouse(getHouse())
                .setStreet(getStreet())
                .setCity(getCity())
                .setPostcode(getPostcode())
                .setPhone(getPhone())
                .create();
    }

    private String getHouse() {
        return mHouseEditText.getText().toString().trim();
    }

    private String getStreet() {
        return mStreetEditText.getText().toString().trim();
    }

    private String getCity() {
        return mCityEditText.getText().toString().trim();
    }

    private String getPostcode() {
        return mPostcodeEditText.getText().toString().trim();
    }

    private String getPhone() {
        String phone = mPhoneEditText.getText().toString().trim();
        return phone.startsWith("+") ? phone : "+" + phone;
    }

    @Override public void showOfferAcceptedInView(Offer offer) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_offer), offer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override public void showHouseError() {
        mHouseInputLayout.setErrorEnabled(true);
        mHouseInputLayout.setError(getText(R.string.shipping_activity_error_house));
    }

    @Override public void showStreetError() {
        mStreetInputLayout.setErrorEnabled(true);
        mStreetInputLayout.setError(getText(R.string.shipping_activity_error_street));
    }

    @Override public void showCityError() {
        mCityInputLayout.setErrorEnabled(true);
        mCityInputLayout.setError(getText(R.string.shipping_activity_error_city));
    }

    @Override public void showPostcodeError() {
        mPostcodeInputLayout.setErrorEnabled(true);
        mPostcodeInputLayout.setError(getText(R.string.shipping_activity_error_postcode));
    }

    @Override public void showPhoneError() {
        mPhoneInputLayout.setErrorEnabled(true);
        mPhoneInputLayout.setError(getText(R.string.shipping_activity_error_phone));
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

    @Override public void setProgressIndicator(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
        mContent.setAlpha(isActive ? 0.5f : 1.0f);
        setTouchContentDisabled(isActive);
    }

    private void setTouchContentDisabled(boolean disable) {
        if (disable) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
