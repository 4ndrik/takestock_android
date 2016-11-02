package com.devabit.takestock.ui.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.devabit.takestock.R;
import com.devabit.takestock.data.filter.AdvertFilter;

import static com.devabit.takestock.data.filter.AdvertFilter.*;

/**
 * Created by Victor Artemyev on 19/05/2016.
 */
public class AdvertSortView extends NestedScrollView {

    private final Unbinder mUnbinder;
    private TextView mActiveTextView;

    public interface OnOrderListener {
        void onOrder(@Order int order);
    }

    private OnOrderListener mOrderListener;

    public AdvertSortView(Context context) {
        this(context, null);
    }

    public AdvertSortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvertSortView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.content_advert_sort_view, AdvertSortView.this);
        mUnbinder = ButterKnife.bind(AdvertSortView.this, view);
    }

    @OnClick({
            R.id.expires_at_text_view, R.id.expires_at_descending_text_view,
            R.id.created_at_text_view, R.id.created_at_descending_text_view,
            R.id.guide_price_text_view,R.id.guide_price_descending_text_view})
    protected void onClick(TextView textView) {
        if (mActiveTextView != null) {
            mActiveTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_grey_24dp, 0);
        }
        mActiveTextView = textView;
        mActiveTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_mark_purple_dark_24dp, 0);
        if(mOrderListener != null) mOrderListener.onOrder(getOrderPerView(mActiveTextView));
    }

    @Order private int getOrderPerView(View view) {
        switch (view.getId()) {
            case R.id.expires_at_text_view:
                return ORDER_EXPIRES_AT;

            case R.id.expires_at_descending_text_view:
                return ORDER_EXPIRES_AT_DESCENDING;

            case R.id.created_at_text_view:
                return ORDER_CREATED_AT;

            case R.id.created_at_descending_text_view:
                return ORDER_CREATED_AT_DESCENDING;

            case R.id.guide_price_text_view:
                return ORDER_GUIDE_PRICE;

            case R.id.guide_price_descending_text_view:
                return ORDER_GUIDE_PRICE_DESCENDING;

            default:
                return AdvertFilter.ORDER_DEFAULT;
        }
    }

    public void setOnOrderListener(OnOrderListener orderListener) {
        mOrderListener = orderListener;
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbinder.unbind();
    }
}
