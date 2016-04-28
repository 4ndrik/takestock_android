package com.devabit.takestock.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;
import com.devabit.takestock.ui.entry.EntryActivity;
import com.devabit.takestock.ui.productDetail.ProductDetailActivity;
import com.devabit.takestock.util.FontCache;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search_products_edit_text) protected EditText mSearchProductsEditText;
    @BindView(R.id.browse_categories_button) protected Button mBrowseProductsButton;
    @BindView(R.id.sell_something_button) protected Button mSellSomethingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);
        Typeface mediumTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_medium);
        mSearchProductsEditText.setTypeface(mediumTypeface);

        Typeface boldTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_bold);
        mBrowseProductsButton.setTypeface(boldTypeface);
        mSellSomethingButton.setTypeface(boldTypeface);
    }

    @OnClick(R.id.sell_something_button)
    protected void startSellingActivvity() {
//        startActivity(new Intent(this, SellingActivity.class));
        startActivity(EntryActivity.getStartIntent(this));
    }

    @OnClick(R.id.browse_categories_button)
    protected void startSearchActivity() {
//        startActivity(new Intent(this, EntryActivity.class));
        startActivity(ProductDetailActivity.getStartIntent(MainActivity.this));
    }

}
