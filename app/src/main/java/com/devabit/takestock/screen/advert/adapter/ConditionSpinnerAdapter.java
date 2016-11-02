package com.devabit.takestock.screen.advert.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.ui.widget.BaseSpinnerAdapter;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class ConditionSpinnerAdapter extends BaseSpinnerAdapter<Condition> {

    public ConditionSpinnerAdapter(Context context, List<Condition> conditions) {
        super(context, conditions);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(getItem(position).getState());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getState());
        return view;
    }
}
