package com.devabit.takestock.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Victor Artemyev on 09/04/2016.
 */
public class RadioButtonGroupTableLayout extends TableLayout implements View.OnClickListener {

    private RadioButton mActiveRadioButton;

    public RadioButtonGroupTableLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RadioButtonGroupTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if (mActiveRadioButton != null) {
            mActiveRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        mActiveRadioButton = rb;
    }

    @Override public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow) child);
    }

    @Override public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }

    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if (v instanceof RadioButton) {
                v.setOnClickListener(this);
            }
        }
    }

    public int getCheckedRadioButtonId() {
        if (mActiveRadioButton != null) {
            return mActiveRadioButton.getId();
        }

        return -1;
    }
}

