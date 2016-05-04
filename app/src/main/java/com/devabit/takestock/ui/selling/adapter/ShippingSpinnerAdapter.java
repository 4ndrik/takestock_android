package com.devabit.takestock.ui.selling.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.model.Shipping;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class ShippingSpinnerAdapter extends SpinnerAdapter<Shipping> {

    private final List<Shipping> mShippings;

    public ShippingSpinnerAdapter(Context context, List<Shipping> shippings) {
        super(context, shippings);
        mShippings = shippings;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(mShippings.get(position).getType());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(mShippings.get(position).getType());
        return view;
    }
}
