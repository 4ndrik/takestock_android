package com.devabit.takestock.screens.advert.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.data.models.Condition;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class ConditionSpinnerAdapter extends SpinnerAdapter<Condition> {

    private final List<Condition> mConditions;

    public ConditionSpinnerAdapter(Context context, List<Condition> conditions) {
        super(context, conditions);
        mConditions = conditions;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(mConditions.get(position).getState());
        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(mConditions.get(position).getState());
        return view;
    }
}
