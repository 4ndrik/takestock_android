package com.devabit.takestock.screen.advert.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.widget.BaseSpinnerAdapter;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class CategorySpinnerAdapter extends BaseSpinnerAdapter<Category> {

    public CategorySpinnerAdapter(Context context, List<Category> categories) {
        super(context, categories);
    }

    @NonNull @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(getItem(position).getName());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }
}
