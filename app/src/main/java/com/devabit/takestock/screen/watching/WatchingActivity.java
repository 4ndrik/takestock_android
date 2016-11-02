package com.devabit.takestock.screen.watching;

import android.content.Context;
import android.content.Intent;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.watching.adapter.WatchingAdvertsAdapter;
import com.devabit.takestock.ui.decoration.ListVerticalSpacingItemDecoration;

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

    WatchingContract.Presenter mPresenter;
    WatchingAdvertsAdapter mAdvertsAdapter;

    boolean mAreAdvertsLoading;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching);
        ButterKnife.bind(WatchingActivity.this);
        setUpToolbar();
        setUpRecyclerView();
        setUpRefreshLayout();
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(WatchingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(WatchingActivity.this, R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                WatchingActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0 || mAreAdvertsLoading) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mAreAdvertsLoading = true;
                    mPresenter.loadAdverts();
                }
            }
        });
        ListVerticalSpacingItemDecoration itemDecoration = new ListVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
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
                mPresenter.removeWatchingAdvert(advert);
            }
        });
        recyclerView.setAdapter(mAdvertsAdapter);
    }

    private void startAdvertDetailActivity(Advert advert) {
        startActivity(AdvertDetailActivity.getStartIntent(WatchingActivity.this, advert.getId()));
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.refreshAdverts();
            }
        });
    }

    private void createPresenter() {
        new WatchingPresenter(
                Injection.provideDataRepository(WatchingActivity.this), WatchingActivity.this);
    }

    @Override public void setPresenter(@NonNull WatchingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.refreshAdverts();
    }

    @Override public void showRefreshedAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.refreshAdverts(adverts);
    }

    @Override public void showLoadedAdvertsInView(List<Advert> adverts) {
        mAreAdvertsLoading = false;
        mAdvertsAdapter.addAdverts(adverts);
    }

    //TODO: refactor
    @Override public void showAdvertRemovedFromWatchingInView(Advert advert) {
        Advert advert1 = mAdvertsAdapter.getAdvertInProcessing(advert.getId());
        if (advert1 != null) {
            mAdvertsAdapter.removeAdvert(advert);
            mAdvertsAdapter.stopAdvertProcessing(advert.getId());
        }
    }

    @Override public void showAdvertRemovedFromWatchingError(Advert advert) {
        mAdvertsAdapter.stopAdvertProcessing(advert.getId());
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

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void setLoadingProgressIndicator(boolean isActive) {
        mAdvertsAdapter.setLoadingProgressEnable(isActive);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}