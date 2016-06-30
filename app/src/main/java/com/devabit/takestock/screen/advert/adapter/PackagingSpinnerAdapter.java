package com.devabit.takestock.screen.advert.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.model.Packaging;
import com.devabit.takestock.widget.BaseSpinnerAdapter;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PackagingSpinnerAdapter extends BaseSpinnerAdapter<Packaging> {

    public PackagingSpinnerAdapter(Context context, List<Packaging> packagings) {
        super(context, packagings);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(getItem(position).getType());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getType());
        return view;
    }
}
