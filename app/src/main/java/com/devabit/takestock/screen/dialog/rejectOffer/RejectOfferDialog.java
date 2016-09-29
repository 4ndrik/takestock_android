package com.devabit.takestock.screen.dialog.rejectOffer;

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
import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class RejectOfferDialog extends DialogFragment {

    private static final String ARG_OFFER = "ARG_OFFER";
    private static final String ARG_FROM_SELLER = "ARG_FROM_SELLER";

    public static RejectOfferDialog newInstance(Offer offer, boolean fromSeller) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_OFFER, offer);
        args.putBoolean(ARG_FROM_SELLER, fromSeller);
        RejectOfferDialog fragment = new RejectOfferDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.comment_edit_text) protected EditText mCommentEditText;

    Unbinder mUnbinder;

    Offer mOffer;
    boolean mFromSeller;

    public interface OnRejectOfferListener {
        void onOfferRejected(RejectOfferDialog dialog, Offer.Accept accept);
    }

    private OnRejectOfferListener mRejectOfferListener;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOffer = getArguments().getParcelable(ARG_OFFER);
        mFromSeller = getArguments().getBoolean(ARG_FROM_SELLER);
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, R.style.AppTheme_Dialog_Alert_Purple);
        builder.setTitle(R.string.reject_offer_dialog_title);
        builder.setPositiveButton(R.string.reject_offer_dialog_reject, null);
        builder.setNegativeButton(R.string.reject_offer_dialog_cancel, null);
        builder.setView(R.layout.dialog_reject_offer);
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
        mUnbinder = ButterKnife.bind(RejectOfferDialog.this, content);
    }

    private void onPositiveButtonClick() {
        if (lacksComment()) return;
        if (mRejectOfferListener != null) mRejectOfferListener.onOfferRejected(RejectOfferDialog.this, createOfferAccept());
    }

    private boolean lacksComment() {
        if (getComment().isEmpty()) {
            mCommentEditText.setError(getString(R.string.reject_offer_dialog_error_comment));
            return true;
        }
        return false;
    }

    private Offer.Accept createOfferAccept() {
        return new Offer.Accept.Builder()
                .setOfferId(mOffer.getId())
                .setStatus(Offer.Status.REJECTED)
                .setFromSeller(mFromSeller)
                .setComment(getComment())
                .create();
    }

    private String getComment() {
        return mCommentEditText.getText().toString().trim();
    }

    public void setOnRejectOfferListener(OnRejectOfferListener rejectOfferListener) {
        mRejectOfferListener = rejectOfferListener;
    }

    private void onNegativeButtonClick() {
        dismiss();
    }

    @Override public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }
}
