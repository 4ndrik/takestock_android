package com.devabit.takestock.screens.selling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.devabit.takestock.R;
import com.devabit.takestock.util.Logger;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class SellingActivity extends AppCompatActivity {

    private static final String TAG = Logger.makeLogTag(SellingActivity.class);

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, SellingActivity.class);
        return starter;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
    }
}
