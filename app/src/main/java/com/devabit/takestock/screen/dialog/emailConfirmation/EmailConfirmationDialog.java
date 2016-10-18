package com.devabit.takestock.screen.dialog.emailConfirmation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 18/10/2016.
 */

public class EmailConfirmationDialog extends DialogFragment {

    private static final String ARG_EMAIL = "ARG_EMAIL";

    public static EmailConfirmationDialog newInstance(String email) {
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        EmailConfirmationDialog fragment = new EmailConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnOkButtonClickListener {
        void onOkClick();
    }

    private OnOkButtonClickListener mOnOkButtonClickListener;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        String email = getArguments().getString(ARG_EMAIL);
        return new AlertDialog.Builder(getActivity())
                .setMessage(buildEmailConfirmationText(email))
                .setPositiveButton(R.string.email_confirmation_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        if (mOnOkButtonClickListener != null) mOnOkButtonClickListener.onOkClick();
                    }
                })
                .create();
    }

    private Spannable buildEmailConfirmationText(String email) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.email_confirmation_dialog_message_part_one));
        builder.append(email);
        builder.setSpan(new StyleSpan(Typeface.BOLD), builder.length() - email.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(getString(R.string.email_confirmation_dialog_message_part_two));
        return builder;
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener onOkButtonClickListener) {
        mOnOkButtonClickListener = onOkButtonClickListener;
    }
}
