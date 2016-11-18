package com.devabit.takestock.screen.payment.byCard;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 17/11/2016.
 */

public class PaymentFailedDialog extends DialogFragment {

    public static PaymentFailedDialog newInstance() {
        return new PaymentFailedDialog();
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Payment failed");
        builder.setMessage("Card payment declined. Please try again, pay by BACS or contact us admin@wetakestock.com");
        builder.setPositiveButton("Contact us", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.admin_email), null));
                contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Payment failed");
                startActivity(Intent.createChooser(contactIntent, "Send email..."));
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }
}
