package com.devabit.takestock.screen.dispatching;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.utils.DateUtil;

import java.util.Calendar;

import static android.view.View.GONE;

/**
 * Created by Victor Artemyev on 07/10/2016.
 */

public class DispatchingActivity extends AppCompatActivity implements DispatchingContract.View {

    private static final String EXTRA_OFFER = "EXTRA_OFFER";

    public static Intent getStartIntent(Context context, Offer offer) {
        Intent starter = new Intent(context, DispatchingActivity.class);
        starter.putExtra(EXTRA_OFFER, offer);
        return starter;
    }

    @BindView(R.id.activity_dispatching_content) ViewGroup mContent;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.arrival_date_text_view) TextView mArrivalDateTextView;
    @BindView(R.id.arrival_date_error_text_view) TextView mArrivalDateErrorTextView;
    @BindView(R.id.pick_up_text_view) TextView mPickUpDateTextView;
    @BindView(R.id.pic_up_date_error_text_view) TextView mPickUpDateErrorTextView;
    @BindView(R.id.tracking_number_edit_text) EditText mTrackingNumberEditText;
    @BindView(R.id.tracking_number_error_text_view) TextView mTrackingNumberErrorTextView;
    @BindView(R.id.courier_name_edit_text) EditText mCourierEditText;
    @BindView(R.id.courier_name_error_text_view) TextView mCourierNameErrorTextView;

    Offer mOffer;
    DispatchingContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching);
        ButterKnife.bind(DispatchingActivity.this);
        mOffer = getIntent().getParcelableExtra(EXTRA_OFFER);
        setUpToolbar();
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(DispatchingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createPresenter() {
        new DispatchingPresenter(Injection.provideDataRepository(DispatchingActivity.this), DispatchingActivity.this);
    }

    @Override public void setPresenter(@NonNull DispatchingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.arrival_date_text_view)
    void onArrivalDateTextViewClick() {
        if (mArrivalDateErrorTextView.isShown()) {
            mArrivalDateErrorTextView.setVisibility(GONE);
        }
        displayDatePickerDialog(mArrivalDateTextView);
    }

    @OnClick(R.id.pick_up_text_view)
    void onPickUpDateTextViewClick() {
        if (mPickUpDateErrorTextView.isShown()) {
            mPickUpDateErrorTextView.setVisibility(GONE);
        }
        displayDatePickerDialog(mPickUpDateTextView);
    }

    private void displayDatePickerDialog(final TextView textView) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(DispatchingActivity.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        textView.setText(getString(R.string.dispatching_activity_date, dayOfMonth, monthOfYear + 1, year));
                    }
                }, year, month, day);
        dialog.show();
    }

    @OnTextChanged(R.id.tracking_number_edit_text)
    void onTrackingNumberTextChanged() {
        if (mTrackingNumberErrorTextView.isShown()) {
            mTrackingNumberErrorTextView.setVisibility(GONE);
        }
    }

    @OnTextChanged(R.id.courier_name_edit_text)
    void onCourierNameTextChanged() {
        if (mCourierNameErrorTextView.isShown()) {
            mCourierNameErrorTextView.setVisibility(GONE);
        }
    }

    @OnClick(R.id.confirm_button)
    void onConfirmButtonClick() {
        mPresenter.acceptOffer(mOffer, createOfferAccept());
    }

    private Offer.Accept createOfferAccept() {
        return new Offer.Accept.Builder()
                .setOfferId(mOffer.getId())
                .setStatus(Offer.Status.STOCK_IN_TRANSIT)
                .setFromSeller(true)
                .setArrivalDate(getArrivalDate())
                .setPickUpDate(getPickUpDate())
                .setTracking(getTrackingNumber())
                .setCourierName(getCourierName())
                .create();
    }

    private String getArrivalDate() {
        return DateUtil.formatToApiDate(mArrivalDateTextView.getText().toString().trim());
    }

    private String getPickUpDate() {
       return DateUtil.formatToApiDate(mPickUpDateTextView.getText().toString().trim());
    }

    private String getTrackingNumber() {
        return mTrackingNumberEditText.getText().toString().trim();
    }

    private String getCourierName() {
        return mCourierEditText.getText().toString().trim();
    }

    @Override public void showOfferAcceptedInView(Offer offer) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_offer), offer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override public void showArrivalDateError() {
        mArrivalDateErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override public void showPickUpDateError() {
        mPickUpDateErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override public void showTrackingNumberError() {
        mTrackingNumberErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override public void showCourierError() {
        mCourierNameErrorTextView.setVisibility(View.VISIBLE);
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
        mProgressBar.setVisibility(isActive ? View.VISIBLE : GONE);
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
