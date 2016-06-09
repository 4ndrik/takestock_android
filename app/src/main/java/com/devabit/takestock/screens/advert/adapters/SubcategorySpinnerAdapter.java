package com.devabit.takestock.screens.advert.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.models.Subcategory;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class SubcategorySpinnerAdapter extends SpinnerAdapter<Subcategory> {

    private final List<Subcategory> mSubcategories;

    public SubcategorySpinnerAdapter(Context context, List<Subcategory> subcategories) {
        super(context, subcategories);
        mSubcategories = subcategories;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(mSubcategories.get(position).getName());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(mSubcategories.get(position).getName());
        return view;
    }
}
