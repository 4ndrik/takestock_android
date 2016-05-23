package com.devabit.takestock.screens.sellSomething.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.models.Category;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class CategorySpinnerAdapter extends SpinnerAdapter<Category> {

    private final List<Category> mCategories;

    public CategorySpinnerAdapter(Context context, List<Category> categories) {
        super(context, categories);
        mCategories = categories;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(mCategories.get(position).getName());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(mCategories.get(position).getName());
        return view;
    }
}
