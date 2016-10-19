package com.devabit.takestock.screen.dialog.accountNotVerified;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 18/10/2016.
 */

public class AccountNotVerifiedDialog extends DialogFragment {

    public static AccountNotVerifiedDialog newInstance() {
        return new AccountNotVerifiedDialog();
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.account_not_activated_dialog_message)
                .setPositiveButton(R.string.account_not_activated_dialog_ok, null)
                .create();
    }
}
