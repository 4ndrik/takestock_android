package com.devabit.takestock.widget;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.devabit.takestock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public abstract class BaseSpinnerAdapter<T> extends ArrayAdapter<T> {

    public BaseSpinnerAdapter(Context context, List<T> objects) {
        super(context, R.layout.item_spinner, new ArrayList<>(objects));
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
