package com.devabit.takestock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProductDetailActivity.class);
    }

    @Bind(R.id.toolbar) protected Toolbar mToolbar;
    @Bind(R.id.content_product_detail) protected View mContent;
    @Bind(R.id.product_image_view) protected ImageView mProductImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(ProductDetailActivity.this);
        mToolbar.setTitle("Test test test test test test");

        mContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        mProductImageView.getLayoutParams().height = mContent.getHeight() / 2;
                        mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @OnClick(R.id.make_offer_fab)
    protected void onMakeOfferFabClick() {
        OfferDialog dialog = OfferDialog.newInstance();
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }
}
