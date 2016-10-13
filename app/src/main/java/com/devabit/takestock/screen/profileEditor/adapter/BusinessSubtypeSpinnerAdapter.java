package com.devabit.takestock.screen.profileEditor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.model.BusinessSubtype;
import com.devabit.takestock.widget.BaseSpinnerAdapter;

import java.util.List;

/**
 * Created by Victor Artemyev on 23/06/2016.
 */
public class BusinessSubtypeSpinnerAdapter extends BaseSpinnerAdapter<BusinessSubtype> {

    public BusinessSubtypeSpinnerAdapter(Context context, List<BusinessSubtype> subtypes) {
        super(context, subtypes);
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
