package com.devabit.takestock.screen.offers.dialogs.counterOffer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
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

    public static CounterOfferDialog newInstance(Advert advert, Offer offer) {
        Bundle args = new Bundle();
        args.putParcelable(Advert.class.getSimpleName(), advert);
        args.putParcelable(Offer.class.getSimpleName(), offer);
        CounterOfferDialog fragment = new CounterOfferDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.price_edit_text) protected EditText mPriceEditText;
    @BindView(R.id.quantity_edit_text) protected EditText mQuantityEditText;
    @BindView(R.id.comment_edit_text) protected EditText mCommentEditText;

    private Unbinder mUnbinder;

    private Advert mAdvert;
    private Offer mOffer;

    public interface OnCounterOfferListener {
        void onOfferCountered(CounterOfferDialog dialog, Offer offer);
    }

    private OnCounterOfferListener mCounterOfferListener;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdvert = getArguments().getParcelable(Advert.class.getSimpleName());
        mOffer = getArguments().getParcelable(Offer.class.getSimpleName());
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, R.style.AppTheme_Dialog_Alert_Purple);
        builder.setTitle(R.string.counter_offer);
        builder.setPositiveButton(R.string.make, null);
        builder.setNegativeButton(R.string.answer_dialog_cancel, null);
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
    }

    private void onPositiveButtonClick() {
        if (isAllValid()) {
            Offer offer = createCounterOffer();
            if (mCounterOfferListener != null) mCounterOfferListener.onOfferCountered(CounterOfferDialog.this, offer);
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
        mPriceEditText.setError(getText(R.string.offer_dialog_error_price));
    }

    private boolean validateQuantity() {
        if (getQuantity().isEmpty()) {
            showEmptyQuantityError();
            return false;
        }
        return true;
    }

    public void showEmptyQuantityError() {
        mQuantityEditText.setError(getText(R.string.offer_dialog_error_quantity));
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

    private Offer createCounterOffer() {
        Offer offer = new Offer.Builder().create();
//        offer.setAdvertId(mAdvert.getId());
//        offer.setCounterOfferId(mOffer.getId());
//        offer.setUserId(mAdvert.getAuthorId());
//        offer.setPrice(getPrice());
//        offer.setQuantity(getQuantityAsInt());
//        offer.setComment(getComment());
//        offer.setStatus(OfferStatus.COUNTERED);
        return offer;
    }

    private void onNegativeButtonClick() {
        dismiss();
    }

    public void setOnCounterOfferListener(OnCounterOfferListener counterOfferListener) {
        mCounterOfferListener = counterOfferListener;
    }

    @Override public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }
}
