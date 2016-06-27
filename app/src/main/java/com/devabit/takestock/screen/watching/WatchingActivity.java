package com.devabit.takestock.screen.watching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 24/06/2016.
 */
public class WatchingActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, WatchingActivity.class);
    }

    Spinner spinner;
    String[] spinnerValue = {
            "PHP",
            "ANDROID",
            "WEB-DESIGN",
            "PHOTOSHOP"
    };

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching);
        spinner =(Spinner)findViewById(R.id.spinner1);


        spinnerAdapter adapter = new spinnerAdapter(WatchingActivity.this, android.R.layout.simple_list_item_1);
        adapter.addAll(spinnerValue);
        adapter.add("This is Hint");
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if(spinner.getSelectedItem() == "This is Hint Text")
                {

                    //Do nothing.
                }
                else{

                    Toast.makeText(WatchingActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }

    public static class spinnerAdapter extends ArrayAdapter<String> {

        public spinnerAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            // TODO Auto-generated constructor stub

        }

        @Override
        public int getCount() {

            int count = super.getCount();

            return count > 0 ? count-1 : count ;


        }


    }
}