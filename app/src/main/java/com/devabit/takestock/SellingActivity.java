package com.devabit.takestock;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.devabit.takestock.util.FontCache;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class SellingActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        toolbar.inflateMenu(R.menu.main);
        TextView title = (TextView) toolbar.findViewById(R.id.title_toolbar);
        Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        title.setTypeface(boldTypeface);
        TextView homeTextView = (TextView) toolbar.findViewById(R.id.home_toolbar);
        Typeface mediumTypeface = FontCache.getTypeface(this, R.string.font_brandon_medium);
        homeTextView.setTypeface(mediumTypeface);
        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
