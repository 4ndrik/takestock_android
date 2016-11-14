package com.devabit.takestock.screen.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Offer;
import com.stripe.android.model.Card;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
public class PayByCardActivity extends AppCompatActivity implements PayByCardContract.View {

    public static Intent getStartIntent(Context context, Offer offer, boolean forwardResult) {
        Intent starter = new Intent(context, PayByCardActivity.class);
        if (forwardResult) starter.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        starter.putExtra(Offer.class.getName(), offer);
        return starter;
    }

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.content_input) protected ViewGroup mContentInput;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;

//    @BindView(R.id.card_number_input_layout) protected TextInputLayout mCardNumberInputLayout;
//    @BindView(R.id.expiry_date_input_layout) protected TextInputLayout mExpiryDateInputLayout;
//    @BindView(R.id.cvv_code_input_layout) protected TextInputLayout mCVVCodeInputLayout;

    @BindView(R.id.email_text_view) TextView mEmailTextView;
    @BindView(R.id.card_number_edit_text) EditText mCardNumberEditText;
    @BindView(R.id.expiry_date_edit_text) EditText mExpiryDateEditText;
    @BindView(R.id.cvv_code_edit_text) EditText mCVVCodeEditText;
    @BindView(R.id.payment_rate_text_view) TextView mPaymentRateTextView;

    Offer mOffer;
    PayByCardContract.Presenter mPresenter;
    int mStripeRate;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_card);
        ButterKnife.bind(PayByCardActivity.this);
        setUpToolbar();
        setUpEmailTextView();
        setUpCardNumberKeyListener();
        mOffer = getIntent().getParcelableExtra(Offer.class.getName());
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(PayByCardActivity.this, R.id.toolbar);
        toolbar.setTitle(R.string.pay_by_card_activity_toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpEmailTextView() {
        mEmailTextView.setText(TakeStockAccount.get(PayByCardActivity.this).getEmail());
    }

    private void setUpCardNumberKeyListener() {
        mCardNumberEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_SPACE;
            }
        });
    }

    private void createPresenter() {
        new PayByCardPresenter(Injection.provideDataRepository(PayByCardActivity.this), PayByCardActivity.this);
    }

    @Override public void setPresenter(@NonNull PayByCardContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.loadPaymentRate();
    }

    @OnTextChanged(value = R.id.card_number_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardNumberAfterTextChanged(Editable text) {
        if (text.length() <= 3) {
            mPresenter.validateCardNumber(text.toString());
        }

        // Remove spacing char
        if (text.length() > 0 && (text.length() % 5) == 0) {
            final char c = text.charAt(text.length() - 1);
            if (' ' == c) {
                text.delete(text.length() - 1, text.length());
            }
        }

        // Insert spacing char where needed.
        if (text.length() > 0 && (text.length() % 5) == 0) {
            char c = text.charAt(text.length() - 1);
            if (Character.isDigit(c) && TextUtils.split(text.toString(), " ").length <= 3) {
                text.insert(text.length() - 1, " ");
            }
        }
    }

    @OnTextChanged(value = R.id.expiry_date_edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onExpiryDateAfterTextChanged(Editable text) {

        if (text.length() > 0 && (text.length() % 3) == 0) {
            final char digit = text.charAt(text.length() - 1);
            if ('/' == digit) {
                text.delete(text.length() - 1, text.length());
            }
        }

        if (text.length() > 0 && (text.length() % 3) == 0) {
            char digit = text.charAt(text.length() - 1);
            if (Character.isDigit(digit)) {
                text.insert(text.length() - 1, "/");
            }
        }
    }

//    @OnTextChanged(R.id.cvv_code_edit_text)
//    protected void onCVVCodeTextChanged() {
//        mCVVCodeInputLayout.setError(null);
//        mCVVCodeInputLayout.setErrorEnabled(false);
//    }

    @Override public void showPaymentRateInView(int rate) {
        mStripeRate = rate;
        mPaymentRateTextView.setText(getString(R.string.pay_by_card_activity_payment_rate, rate));
        setUpPayButton(mStripeRate);
    }

    private void setUpPayButton(int stripeRate) {
        Button button = ButterKnife.findById(PayByCardActivity.this, R.id.pay_button);
        button.setText(getString(R.string.pay_by_card_activity_make_payment, getPrice(stripeRate)));
    }

    private double getPrice(int paymentRate) {
        double price = Double.parseDouble(mOffer.getPrice());
        double finalPrice = price * mOffer.getQuantity();
        double surcharge = paymentRate / 100.0;
        return finalPrice + (finalPrice * surcharge);
    }

    @Override public void showOfferPaidInView(Offer offer) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_offer), offer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override public void showCardAmericanExpressInView() {
        setCardDrawableRes(R.drawable.ic_card_american_express_black_24dp);
    }

    @Override public void showCardDiscoverInView() {
        setCardDrawableRes(R.drawable.ic_card_discover_black_24dp);
    }

    @Override public void showCardJCBInView() {
        setCardDrawableRes(R.drawable.ic_card_jcb_black_24dp);
    }

    @Override public void showCardDinnersClubInView() {
        setCardDrawableRes(R.drawable.ic_card_diners_club_black_24dp);
    }

    @Override public void showCardVisaInView() {
        setCardDrawableRes(R.drawable.ic_card_visa_black_24dp);
    }

    @Override public void showCardMastercardInView() {
        setCardDrawableRes(R.drawable.ic_card_mastercard_black_24dp);
    }

    @Override public void showCardUnknownInView() {
        setCardDrawableRes(R.drawable.ic_card_unknown_grey600_24dp);
    }

    private void setCardDrawableRes(@DrawableRes int resId) {
        mCardNumberEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0);
    }

    @OnClick(R.id.pay_button)
    protected void onPayButtonClick() {
        mPresenter.makePayment(mOffer, getCard());
    }

    private Card getCard() {
        String number = getCardNumber();
        Pair<Integer, Integer> expiryDate = getExpiryDate();
        String cvvCode = getCVVCode();
        return new Card(number, expiryDate.first, expiryDate.second, cvvCode);
    }

    private String getCardNumber() {
        return mCardNumberEditText.getText().toString();
    }

    private Pair<Integer, Integer> getExpiryDate() {
        String[] source = mExpiryDateEditText
                .getText()
                .toString()
                .split("\\/");
        if (source.length == 2) {
            Integer month = getInteger(source[0]);
            Integer year = getInteger(source[1]);
            return Pair.create(month, year);
        } else {
            return Pair.create(0, 0);
        }
    }

    private Integer getInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Timber.e(e);
            return 0;
        }
    }

    private String getCVVCode() {
        return mCVVCodeEditText.getText().toString();
    }

    @Override public void showCardNumberError() {
        mCardNumberEditText.setError("The card number is invalid");
//        mCardNumberInputLayout.setErrorEnabled(true);
//        mCardNumberInputLayout.setError();
    }

    @Override public void showExpiryDateError() {
//        mExpiryDateInputLayout.setErrorEnabled(true);
        mExpiryDateEditText.setError("The expiration date is invalid");
    }

    @Override public void showCVVCodeError() {
//        mCVVCodeInputLayout.setErrorEnabled(true);
        mCVVCodeEditText.setError("The CVC code is invalid");
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    @Override public void showPaymentError() {
        showSnack(R.string.pay_by_card_activity_error_payment);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        setProgressBarActive(isActive);
        setTouchDisabled(isActive);
        setContentInputTransparency(isActive);
    }

    private void setProgressBarActive(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
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

    private void setContentInputTransparency(boolean isActive) {
        mContentInput.clearFocus();
        for (int i = 0; i < mContentInput.getChildCount(); i++) {
            View view = mContentInput.getChildAt(i);
            view.setAlpha(isActive ? 0.5f : 1.0f);
        }
    }

    @Override protected void onStop() {
        mPresenter.pause();
        super.onStop();
    }
}
