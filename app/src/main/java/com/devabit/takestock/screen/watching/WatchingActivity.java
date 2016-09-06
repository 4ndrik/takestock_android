package com.devabit.takestock.screen.watching;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.watching.adapter.WatchingAdvertsAdapter;
import com.devabit.takestock.utils.FontCache;
import com.devabit.takestock.widget.ListSpacingItemDecoration;

import java.util.List;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 24/06/2016.
 */
public class WatchingActivity extends AppCompatActivity implements WatchingContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, WatchingActivity.class);
    }

    @BindView(R.id.content) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private WatchingContract.Presenter mPresenter;
    private WatchingAdvertsAdapter mAdvertsAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching);
        ButterKnife.bind(WatchingActivity.this);
        new WatchingPresenter(
                Injection.provideDataRepository(WatchingActivity.this), WatchingActivity.this);
        setUpToolbar();
        setUpAdvertsRecyclerView();
        setUpRefreshLayout();
    }

    @Override public void setPresenter(@NonNull WatchingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void setUpToolbar() {
        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        Toolbar toolbar = ButterKnife.findById(WatchingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.watching);
    }

    private void setUpAdvertsRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(WatchingActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                WatchingActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ListSpacingItemDecoration itemDecoration = new ListSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        mAdvertsAdapter = new WatchingAdvertsAdapter(WatchingActivity.this);
        mAdvertsAdapter.setOnItemClickListener(new WatchingAdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Advert advert) {
                startAdvertDetailActivity(advert);
            }
        });
        mAdvertsAdapter.setOnWatchedChangeListener(new WatchingAdvertsAdapter.OnWatchedChangeListener() {
            @Override public void onRemoved(Advert advert) {
                mAdvertsAdapter.startAdvertProcessing(advert);
                mPresenter.removeWatchingAdvert(advert.getId());
            }
        });
        recyclerView.setAdapter(mAdvertsAdapter);
    }

    private void startAdvertDetailActivity(Advert advert) {
        startActivity(AdvertDetailActivity.getStartIntent(WatchingActivity.this, advert));
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mAdvertsAdapter.clearAdverts();
                mPresenter.fetchAdverts();
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.resume();
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mAdvertsAdapter.destroy();
    }

    @Override public void showAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.clearAdverts();
        mAdvertsAdapter.addAdverts(adverts);
    }

    @Override public void showAdvertRemovedFromWatchingInView(int advertId) {
        Advert advert = mAdvertsAdapter.getAdvertInProcessing(advertId);
        if (advert != null) {
            mAdvertsAdapter.removeAdvert(advert);
            mAdvertsAdapter.stopAdvertProcessing(advertId);
        }
    }

    @Override public void showAdvertRemovedFromWatchingError(int advertId) {
        mAdvertsAdapter.stopAdvertProcessing(advertId);
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
}