package com.devabit.takestock.screen.advert.buying;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 16/11/2016.
 */

public class BACSDetailDialog extends DialogFragment {

    private static final String ARGUMENT_OFFER = "OFFER";
    private static final String ARGUMENT_ADVERT_NAME = "ADVERT_NAME";

    public static BACSDetailDialog newInstance(Offer offer, String advertName) {
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_OFFER, offer);
        args.putString(ARGUMENT_ADVERT_NAME, advertName);
        BACSDetailDialog fragment = new BACSDetailDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("BACS Detail");
        builder.setView(R.layout.content_bacs_detail);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }

    @Override public void onStart() {
        super.onStart();
        TextView textView = (TextView) getDialog().findViewById(R.id.info_bank_text_view);
        textView.setText(buildBankInfoSpannable());
    }

    private Spannable buildBankInfoSpannable() {
        String advertName = getArguments().getString(ARGUMENT_ADVERT_NAME);
        if (advertName == null) return new SpannableString("");
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.pay_by_bacs_activity_info_bank_part_one));
        builder.append(advertName);
        builder.setSpan(new StyleSpan(Typeface.BOLD), builder.length() - advertName.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(getString(R.string.pay_by_bacs_activity_info_bank_part_two));
        return builder;
    }
}
