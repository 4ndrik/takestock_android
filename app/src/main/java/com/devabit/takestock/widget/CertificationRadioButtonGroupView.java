package com.devabit.takestock.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Certification;

import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class CertificationRadioButtonGroupView extends TableLayout implements View.OnClickListener{

    private RadioButton mActiveRadioButton;

    public CertificationRadioButtonGroupView(Context context) {
        super(context);
    }

    public CertificationRadioButtonGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUpCertifications(List<Certification> certifications) {
        Context context = getContext();
        TableRow tableRow = null;
        for (int index = 0; index < certifications.size(); index++) {

            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(certifications.get(index).getName());
            radioButton.setOnClickListener(CertificationRadioButtonGroupView.this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (index % 2 == 0) {
                tableRow = new TableRow(context);
                addView(tableRow);
                params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dimen_48dp);
                radioButton.setLayoutParams(params);
                tableRow.addView(radioButton);
            } else if (tableRow != null){
                radioButton.setLayoutParams(params);
                tableRow.addView(radioButton);
            }
        }
    }

    @Override public void onClick(View v) {
        final RadioButton radioButton = (RadioButton) v;
        if (mActiveRadioButton != null) {
            mActiveRadioButton.setChecked(false);
        }
        radioButton.setChecked(true);
        mActiveRadioButton = radioButton;
    }
}
