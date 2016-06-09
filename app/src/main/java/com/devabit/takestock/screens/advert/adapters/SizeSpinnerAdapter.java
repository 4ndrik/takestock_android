package com.devabit.takestock.screens.advert.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.models.Size;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class SizeSpinnerAdapter extends SpinnerAdapter<Size> {

    private final List<Size> mSizes;

    public SizeSpinnerAdapter(Context context, List<Size> sizes) {
        super(context, sizes);
        mSizes = sizes;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(mSizes.get(position).getType());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(mSizes.get(position).getType());
        return view;
    }
}
