package com.devabit.takestock.screens.buying;

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
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.OfferStatus;
import com.devabit.takestock.screens.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screens.buying.adapters.OfferAdvertPairsAdapter;
import com.devabit.takestock.utils.FontCache;
import com.devabit.takestock.utils.Logger;

import java.util.Map;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public class BuyingActivity extends AppCompatActivity implements BuyingContract.View {

    private static final String TAG = Logger.makeLogTag(BuyingActivity.class);

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, BuyingActivity.class);
        return starter;
    }

    @BindView(R.id.content_activity_buying) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private BuyingContract.Presenter mPresenter;

    private OfferAdvertPairsAdapter mOffersAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        ButterKnife.bind(BuyingActivity.this);
        initPresenter();
        setUpToolbar();
        setUpRefreshLayout();
        mPresenter.create();
    }

    private void initPresenter() {
        new BuyingPresenter(
                Injection.provideDataRepository(BuyingActivity.this), BuyingActivity.this);
    }

    private void setUpToolbar() {
        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        Toolbar toolbar = ButterKnife.findById(BuyingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.buying);
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshOffers();
            }
        });
    }

    private void refreshOffers() {
        mOffersAdapter.clearOffers();
        fetchOffers();
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.resume();
    }

    @Override public void showOfferStatusesInView(SparseArray<OfferStatus> statuses) {
        setUpOffersRecyclerView(statuses);
        fetchOffers();
    }

    private void setUpOffersRecyclerView(SparseArray<OfferStatus> statuses) {
        RecyclerView recyclerView = ButterKnife.findById(BuyingActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                BuyingActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mOffersAdapter = new OfferAdvertPairsAdapter(BuyingActivity.this, statuses);
        mOffersAdapter.setMenuItemClickListener(mMenuItemClickListener);
        recyclerView.setAdapter(mOffersAdapter);
    }

    private final OfferAdvertPairsAdapter.OnMenuItemClickListener mMenuItemClickListener
            = new OfferAdvertPairsAdapter.OnMenuItemClickListener() {
        @Override public void viewAdvert(Advert advert) {
            startAdvertDetailActivity(advert);
        }
    };

    private void startAdvertDetailActivity(Advert advert) {
        startActivity(AdvertDetailActivity.getStartIntent(BuyingActivity.this, advert));
    }

    private void fetchOffers() {
        mPresenter.fetchOffersByUserId(getUserId());
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(BuyingActivity.this);
        Account account = accountManager.getAccountsByType(getString(R.string.authenticator_account_type))[0];
        String userId = accountManager.getUserData(account, getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    @Override public void showOfferAdvertPairsInView(Map<Offer, Advert> offerAdvertMap) {
        mOffersAdapter.addOffers(offerAdvertMap);
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

    @Override public void setPresenter(@NonNull BuyingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mOffersAdapter.destroy();
    }
}
