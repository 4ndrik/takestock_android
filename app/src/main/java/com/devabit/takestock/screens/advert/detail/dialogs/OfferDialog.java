package com.devabit.takestock.screens.advert.detail.dialogs;

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
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 14/04/2016.
 */
public class OfferDialog extends DialogFragment implements OfferContract.View {

    private static final String TAG = makeLogTag(OfferDialog.class);

    private static final String ARG_USER_ID = "USER_ID";
    private static final String ARG_ADVERT = "ADVERT";

    public static OfferDialog newInstance(int userId, Advert advert) {
        Bundle args = new Bundle();
        OfferDialog dialog = new OfferDialog();
        args.putInt(ARG_USER_ID, userId);
        args.putParcelable(ARG_ADVERT, advert);
        dialog.setArguments(args);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.setCancelable(true);
        return dialog;
    }

    private int mUserId;
    private Advert mAdvert;

    public interface OnOfferListener {
        void onOfferMade(OfferDialog dialog, Offer offer);
    }

    private OnOfferListener mOfferListener;

    private Unbinder mUnbinder;
    private OfferContract.Presenter mPresenter;

    @BindView(R.id.quantity_edit_text) protected EditText mQuantityEditText;
    @BindView(R.id.price_edit_text) protected EditText mPriceEditText;
    @BindView(R.id.total_price_text_view) protected TextView mTotalPriceTextView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUserId = args.getInt(ARG_USER_ID);
        mAdvert = args.getParcelable(ARG_ADVERT);
        new OfferPresenter(OfferDialog.this);
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, R.style.AppTheme_Dialog_Alert_Purple);
        builder.setTitle(R.string.offer);
        builder.setPositiveButton(R.string.make, null);
        builder.setNegativeButton(R.string.cancel, null);
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
        Offer offer = new Offer();
        offer.setAdvertId(mAdvert.getId());
        offer.setUserId(mUserId);
        offer.setQuantity(getQuantityAsInt());
        offer.setPrice(getPrice());
        offer.setOfferStatusId(3);
        return offer;
    }

    private int getQuantityAsInt() {
        if (getQuantity().isEmpty()) return 0;
        else return Integer.valueOf(getQuantity());
    }

    @OnTextChanged(R.id.quantity_edit_text)
    protected void onQuantityTextChanged(CharSequence text) {
        LOGD(TAG, "onQuantityTextChanged: " + text);
        calculateTotalPrice();
    }

    @OnTextChanged(R.id.price_edit_text)
    protected void onPriceTextChanged(CharSequence text) {
        LOGD(TAG, "onPriceTextChanged: " + text);
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
        mTotalPriceTextView.setText(getString(R.string.total_price, quantity, totalPrice));
    }

    @Override public void showEmptyQuantityError() {
        mQuantityEditText.setError(getText(R.string.error_empty_quantity));
    }

    @Override public void showEmptyPriceError() {
        mPriceEditText.setError(getText(R.string.error_empty_price));
    }

    @Override public void setPresenter(@NonNull OfferContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setOnOfferListener(OnOfferListener offerListener) {
        mOfferListener = offerListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
