package com.devabit.takestock.screen.advert.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.widgets.BaseSpinnerAdapter;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class SubcategorySpinnerAdapter extends BaseSpinnerAdapter<Subcategory> {

    public SubcategorySpinnerAdapter(Context context, List<Subcategory> subcategories) {
        super(context, subcategories);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(getItem(position).getName());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }
}
