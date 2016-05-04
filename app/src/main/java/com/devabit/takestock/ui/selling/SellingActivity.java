package com.devabit.takestock.ui.selling;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.util.FontCache;

import java.util.List;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class SellingActivity extends AppCompatActivity implements SellingContract.View {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.add_image_product_button_activity_selling) protected Button mAddImageButton;
    @BindView(R.id.expiry_text_view) protected TextView mExpiryTextView;

    private SellingContract.Presenter mPresenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);
        setUpToolbar();

        Typeface boldTypeface = FontCache.getTypeface(SellingActivity.this, R.string.font_brandon_bold);
        mAddImageButton.setTypeface(boldTypeface);

        new SellingPresenter(
                Injection.provideDataRepository(SellingActivity.this), SellingActivity.this);
        mPresenter.create();
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

    @Override public void setPresenter(SellingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showCategoriesInView(List<Category> categories) {

    }

    @Override public void showShippingsInView(List<Shipping> shippings) {

    }

    @Override public void showConditionsInView(List<Condition> conditions) {

    }

    @Override public void showSizesInView(List<Size> sizes) {

    }

    @Override public void showCertificationsInView(List<Certification> certifications) {

    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
