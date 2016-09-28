package com.devabit.takestock.screen.advert.detail.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;

import static timber.log.Timber.d;

/**
 * Created by Victor Artemyev on 14/04/2016.
 */
public class OfferDialog extends DialogFragment implements OfferContract.View {

    private static final String ARG_ADVERT = "ADVERT";

    public static OfferDialog newInstance(Advert advert) {
        Bundle args = new Bundle();
        OfferDialog dialog = new OfferDialog();
        args.putParcelable(ARG_ADVERT, advert);
        dialog.setArguments(args);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.setCancelable(true);
        return dialog;
    }

    private Advert mAdvert;

    public interface OnOfferListener {
        void onOfferMade(OfferDialog dialog, Offer offer);
    }

    private OnOfferListener mOfferListener;

    private Unbinder mUnbinder;
    private OfferContract.Presenter mPresenter;

    @BindView(R.id.quantity_edit_text) protected EditText mQuantityEditText;
    @BindView(R.id.quantity_text_view) protected TextView mQuantityTextView;
    @BindView(R.id.price_edit_text) protected EditText mPriceEditText;
    @BindView(R.id.packaging_text_view) protected TextView mPackagingTextView;
    @BindView(R.id.per_packaging_text_view) protected TextView mPerPackagingTextView;
    @BindView(R.id.total_price_text_view) protected TextView mTotalPriceTextView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAdvert = args.getParcelable(ARG_ADVERT);
        createPresenter();
    }

    private void createPresenter() {
        new OfferPresenter(OfferDialog.this);
    }

    @Override public void setPresenter(@NonNull OfferContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, R.style.AppTheme_Dialog_Alert_Purple);
        builder.setTitle(R.string.offer_dialog_title);
        builder.setPositiveButton(R.string.offer_dialog_make, null);
        builder.setNegativeButton(R.string.offer_dialog_cancel, null);
        builder.setView(R.layout.dialog_offer);
        return builder.create();
    }

    @Override public void onStart() {
        super.onStart();
        setUpDialog();
    }

    private void setUpDialog() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onPositiveButtonClick();
            }
        });
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onNegativeButtonClick();
            }
        });
        View content = ButterKnife.findById(dialog, R.id.content);
        mUnbinder = ButterKnife.bind(OfferDialog.this, content);
        mQuantityTextView.setText(getString(R.string.offer_dialog_quantity_available, mAdvert.getItemsCountNow()));
        mPackagingTextView.setText(mAdvert.getPackagingName());
        mPerPackagingTextView.setText(getString(R.string.offer_dialog_per_packaging, mAdvert.getPackagingName()));
        calculateTotalPrice();
    }

    private void onPositiveButtonClick() {
        Offer offer = createOffer();
        mPresenter.validateOffer(offer);
    }

    private void onNegativeButtonClick() {
        dismiss();
    }

    private Offer createOffer() {
        return new Offer.Builder()
                .setAdvertId(mAdvert.getId())
                .setUserId(getUserId())
                .setQuantity(getQuantityAsInt())
                .setPrice(getPrice())
                .setStatus(Offer.Status.PENDING)
                .create();
    }

    private int getUserId() {
        return TakeStockAccount.get(getActivity()).getUserId();
    }

    private int getQuantityAsInt() {
        if (getQuantity().isEmpty()) return 0;
        else return Integer.valueOf(getQuantity());
    }

    @OnTextChanged(R.id.quantity_edit_text)
    protected void onQuantityTextChanged(CharSequence text) {
        d("onQuantityTextChanged: %s", text);
        if (text.length() > 0) {
            int quantity = Integer.parseInt(text.toString());
            if (quantity > mAdvert.getItemsCountNow()) {
                mQuantityEditText.getText().clear();
                mQuantityEditText.setError("Quantity greater than " + mAdvert.getItemsCount());
            }
            calculateTotalPrice();
        }
    }

    @OnTextChanged(R.id.price_edit_text)
    protected void onPriceTextChanged(CharSequence text) {
        d("onPriceTextChanged: %s", text);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        mPresenter.calculateOfferTotalPrice(getQuantity(), getPrice());
    }

    private String getQuantity() {
        return mQuantityEditText.getText().toString().trim();
    }

    private String getPrice() {
        return mPriceEditText.getText().toString().trim();
    }

    @Override public void showValidOffer(Offer offer) {
        if (mOfferListener != null) mOfferListener.onOfferMade(OfferDialog.this, offer);
    }

    @Override public void showTotalPriceInView(String quantity, double totalPrice) {
        mTotalPriceTextView.setText(
                getString(R.string.offer_dialog_total_price, quantity, mAdvert.getPackagingName(), totalPrice));
    }

    @Override public void showEmptyQuantityError() {
        mQuantityEditText.setError(getText(R.string.offer_dialog_error_quantity));
    }

    @Override public void showEmptyPriceError() {
        mPriceEditText.setError(getText(R.string.offer_dialog_error_price));
    }

    public void setOnOfferListener(OnOfferListener offerListener) {
        mOfferListener = offerListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
