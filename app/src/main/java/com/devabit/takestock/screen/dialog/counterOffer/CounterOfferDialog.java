package com.devabit.takestock.screen.dialog.counterOffer;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class CounterOfferDialog extends DialogFragment {

    private static final String ARG_ADVERT = "ARG_ADVERT";
    private static final String ARG_OFFER = "ARG_OFFER";
    private static final String ARG_FROM_SELLER = "ARG_FROM_SELLER";

    public static CounterOfferDialog newInstance(Advert advert, Offer offer, boolean fromSeller) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ADVERT, advert);
        args.putParcelable(ARG_OFFER, offer);
        args.putBoolean(ARG_FROM_SELLER, fromSeller);
        CounterOfferDialog fragment = new CounterOfferDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.price_edit_text) protected EditText mPriceEditText;
    @BindView(R.id.quantity_edit_text) protected EditText mQuantityEditText;
    @BindView(R.id.comment_edit_text) protected EditText mCommentEditText;
    @BindView(R.id.quantity_unit_text_view) protected TextView mQuantityUnitTextView;

    Unbinder mUnbinder;

    Advert mAdvert;
    Offer mOffer;
    boolean mFromSeller;

    public interface OnOfferCounteredListener {
        void onCountered(CounterOfferDialog dialog, Offer.Accept accept);
    }

    OnOfferCounteredListener mOfferCounteredListener;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdvert = getArguments().getParcelable(ARG_ADVERT);
        mOffer = getArguments().getParcelable(ARG_OFFER);
        mFromSeller = getArguments().getBoolean(ARG_FROM_SELLER, false);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, R.style.AppTheme_Dialog_Alert_Purple);
//        builder.setTitle(R.string.counter_offer_dialog_title);
        builder.setPositiveButton(R.string.counter_offer_dialog_send, null);
        builder.setNegativeButton(R.string.counter_offer_dialog_cancel, null);
        builder.setView(R.layout.dialog_counter_offer);
        return builder.create();
    }

    @Override public void onStart() {
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
        mUnbinder = ButterKnife.bind(CounterOfferDialog.this, content);
        mQuantityUnitTextView.setText(mAdvert.getPackagingName());
    }

    private void onPositiveButtonClick() {
        if (isAllValid()) {
            if (mOfferCounteredListener != null)
                mOfferCounteredListener.onCountered(CounterOfferDialog.this, createOfferAccept());
        }
    }

    private boolean isAllValid() {
        return validatePrice()
                && validateQuantity();
    }

    private boolean validatePrice() {
        if (getPrice().isEmpty()) {
            showEmptyPriceError();
            return false;
        }
        return true;
    }

    public void showEmptyPriceError() {
        mPriceEditText.setError(getText(R.string.counter_offer_dialog_error_price));
    }

    private boolean validateQuantity() {
        if (getQuantity().isEmpty()) {
            showEmptyQuantityError();
            return false;
        }
        return true;
    }

    public void showEmptyQuantityError() {
        mQuantityEditText.setError(getText(R.string.counter_offer_dialog_error_quantity));
    }

    private Offer.Accept createOfferAccept() {
        return new Offer.Accept.Builder()
                .setOfferId(mOffer.getId())
                .setStatus(mFromSeller ? Offer.Status.COUNTERED : Offer.Status.COUNTERED_BY_BUYER)
                .setFromSeller(mFromSeller)
                .setQuantity(getQuantityAsInt())
                .setPrice(getPrice())
                .setComment(getComment())
                .create();
    }

    private String getPrice() {
        return mPriceEditText.getText().toString().trim();
    }

    private int getQuantityAsInt() {
        if (getQuantity().isEmpty()) return 0;
        else return Integer.valueOf(getQuantity());
    }

    private String getQuantity() {
        return mQuantityEditText.getText().toString().trim();
    }

    private String getComment() {
        return mCommentEditText.getText().toString().trim();
    }

    private void onNegativeButtonClick() {
        dismiss();
    }

    public void setOnOfferCounteredListener(OnOfferCounteredListener offerCounteredListener) {
        mOfferCounteredListener = offerCounteredListener;
    }

    @Override public void onStop() {
        mUnbinder.unbind();
        super.onStop();
    }
}
