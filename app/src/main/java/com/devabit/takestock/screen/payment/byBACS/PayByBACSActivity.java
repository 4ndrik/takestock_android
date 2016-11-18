package com.devabit.takestock.screen.payment.byBACS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.screen.payment.byCard.PayByCardActivity;

/**
 * Created by Victor Artemyev on 20/10/2016.
 */

public class PayByBACSActivity extends AppCompatActivity implements PayByBACSContract.View {

    private static final String EXTRA_OFFER = "EXTRA_OFFER";
    private static final String EXTRA_ADVERT_NAME = "EXTRA_ADVERT_NAME";

    public static Intent getStartIntent(Context context, Offer offer, String advertName) {
        Intent starter = new Intent(context, PayByBACSActivity.class);
        starter.putExtra(EXTRA_OFFER, offer);
        starter.putExtra(EXTRA_ADVERT_NAME, advertName);
        return starter;
    }

    @BindView(R.id.content) ViewGroup mContent;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    Offer mOffer;
    PayByBACSContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_bacs);
        ButterKnife.bind(PayByBACSActivity.this);
        setUpToolbar();
        mOffer = getIntent().getParcelableExtra(EXTRA_OFFER);
        setUpBankInfoTextView(getIntent().getStringExtra(EXTRA_ADVERT_NAME));
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(PayByBACSActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpBankInfoTextView(String advertName) {
        TextView textView = ButterKnife.findById(PayByBACSActivity.this, R.id.info_bank_text_view);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.pay_by_bacs_activity_info_bank_part_one));
        builder.append(advertName);
        builder.setSpan(new StyleSpan(Typeface.BOLD), builder.length() - advertName.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(getString(R.string.pay_by_bacs_activity_info_bank_part_two));
        textView.setText(builder);
    }

    private void createPresenter() {
        new PayByBACSPresenter(Injection.provideDataRepository(PayByBACSActivity.this), PayByBACSActivity.this);
    }

    @Override public void setPresenter(@NonNull PayByBACSContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.pay_by_card_button)
    void onPayByCardButtonClick() {
        startActivity(PayByCardActivity.getStartIntent(PayByBACSActivity.this, mOffer, true));
        finish();
    }

    @OnClick(R.id.pay_by_bacs_button)
    void onPayByBACSButtonClick() {
        mPresenter.makePayment(createPayment());
    }

    private Payment createPayment() {
        return new Payment(mOffer.getId(), "", Payment.Type.BACS);
    }

    @Override public void showOfferPaidInView(Offer offer) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.extra_offer), offer);
            setResult(RESULT_OK, intent);
            finish();
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showPaymentError() {
        showSnack(R.string.pay_by_bacs_activity_error_payment);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        setProgressBarActive(isActive);
        setTouchDisabled(isActive);
        setContentAlpha(isActive);
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

    private void setContentAlpha(boolean isActive) {
        mContent.setAlpha(isActive ? 0.5f : 1f);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
