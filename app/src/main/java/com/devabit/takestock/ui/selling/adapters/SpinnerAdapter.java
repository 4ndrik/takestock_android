package com.devabit.takestock.ui.selling.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.devabit.takestock.R;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public abstract class SpinnerAdapter<T> extends ArrayAdapter<T> {

    public SpinnerAdapter(Context context, List<T> objects) {
        super(context, R.layout.item_spinner_promt, objects);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
