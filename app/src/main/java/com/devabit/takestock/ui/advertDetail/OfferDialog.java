package com.devabit.takestock.ui.advertDetail;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 14/04/2016.
 */
public class OfferDialog extends DialogFragment {

    private Unbinder mUnbinder;

    public static OfferDialog newInstance() {
        Bundle args = new Bundle();

        OfferDialog dialog = new OfferDialog();
        dialog.setArguments(args);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_offer, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(OfferDialog.this, view);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
