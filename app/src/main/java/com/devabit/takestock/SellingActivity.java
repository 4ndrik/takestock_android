package com.devabit.takestock;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.devabit.takestock.util.FontCache;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class SellingActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) protected Toolbar mToolbar;
    @Bind(R.id.add_image_product_button_activity_selling) protected Button mAddImageButton;
    @Bind(R.id.expiry_text_view) protected TextView mExpiryTextView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);
        setUpToolbar();

        Typeface boldTypeface = FontCache.getTypeface(SellingActivity.this, R.string.font_brandon_bold);
        mAddImageButton.setTypeface(boldTypeface);

    }

    private void setUpToolbar() {
        mToolbar.inflateMenu(R.menu.main);
        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        title.setTypeface(boldTypeface);
        title.setText(R.string.sell_something);
        TextView homeTextView = (TextView) mToolbar.findViewById(R.id.toolbar_back);
        Typeface mediumTypeface = FontCache.getTypeface(this, R.string.font_brandon_medium);
        homeTextView.setTypeface(mediumTypeface);
        homeTextView.setText(R.string.home);
        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
