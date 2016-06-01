package com.devabit.takestock.screens.selling;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.screens.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screens.selling.adapters.AdvertsAdapter;
import com.devabit.takestock.util.FontCache;
import com.devabit.takestock.util.Logger;

import java.util.List;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class SellingActivity extends AppCompatActivity implements SellingContract.View {

    private static final String TAG = Logger.makeLogTag(SellingActivity.class);

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, SellingActivity.class);
        return starter;
    }

    @BindView(R.id.content_activity_selling) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private SellingContract.Presenter mPresenter;
    private AdvertsAdapter mAdvertsAdapter;
    private AdvertFilter mAdvertFilter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);

        new SellingPresenter(
                Injection.provideDataRepository(SellingActivity.this), SellingActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        Toolbar toolbar = ButterKnife.findById(SellingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.selling);

        RecyclerView recyclerView = ButterKnife.findById(SellingActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                SellingActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdvertsAdapter = new AdvertsAdapter(SellingActivity.this);
        mAdvertsAdapter.setOnEndPositionListener(new AdvertsAdapter.OnEndPositionListener() {
            @Override public void onEndPosition(int position) {
                mPresenter.fetchAdverts();
            }
        });
        mAdvertsAdapter.setOnItemClickListener(mMenuItemClickListener);
        recyclerView.setAdapter(mAdvertsAdapter);

        mAdvertFilter = getAdvertFilter();
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mAdvertsAdapter.clearAdverts();
                mPresenter.fetchAdvertsPerFilter(mAdvertFilter);
            }
        });
        mPresenter.fetchAdvertsPerFilter(mAdvertFilter);
    }

    private final AdvertsAdapter.OnMenuItemClickListener mMenuItemClickListener
            = new AdvertsAdapter.OnMenuItemClickListener() {
        @Override public void viewAdvert(Advert advert) {
            startAdvertDetailActivity(advert);
        }

        @Override public void editAdvert(Advert advert) {
            Toast.makeText(SellingActivity.this, "Not yet implemented.", Toast.LENGTH_LONG).show();
        }
    };

    private void startAdvertDetailActivity(Advert advert) {
        startActivity(AdvertDetailActivity.getStartIntent(SellingActivity.this, advert));
    }

    private AdvertFilter getAdvertFilter() {
        AdvertFilter filter = new AdvertFilter();
        filter.setAuthorId(getUserId());
        return filter;
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(SellingActivity.this);
        Account account = accountManager.getAccountsByType(getString(R.string.authenticator_account_type))[0];
        String userId = accountManager.getUserData(account, getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    @Override public void showAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.addAdverts(adverts);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mAdvertsAdapter.destroy();
    }
}
