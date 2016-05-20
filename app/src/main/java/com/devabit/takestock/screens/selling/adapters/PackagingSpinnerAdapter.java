package com.devabit.takestock.screens.selling.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.models.Packaging;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PackagingSpinnerAdapter extends SpinnerAdapter<Packaging> {

    private final List<Packaging> mPackagings;

    public PackagingSpinnerAdapter(Context context, List<Packaging> packagings) {
        super(context, packagings);
        mPackagings = packagings;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(mPackagings.get(position).getType());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(mPackagings.get(position).getType());
        return view;
    }
}
