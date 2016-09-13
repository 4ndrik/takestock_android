package com.devabit.takestock.screen.adverts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.screen.adverts.adapter.AdvertsAdapter;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.widget.GridSpacingItemDecoration;
import timber.log.Timber;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public class AdvertsActivity extends AppCompatActivity implements AdvertsContract.View {

    private static final String EXTRA_CATEGORY = Category.class.getName();
    private static final String EXTRA_SUBCATEGORY = Subcategory.class.getName();

    public static Intent getStartIntent(Context context, Category category, Subcategory subcategory) {
        Intent starter = new Intent(context, AdvertsActivity.class);
        starter.putExtra(EXTRA_CATEGORY, category);
        starter.putExtra(EXTRA_SUBCATEGORY, subcategory);
        return starter;
    }

    @BindView(R.id.content) protected ViewGroup mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    TakeStockAccount mAccount;
    AdvertsAdapter mAdvertsAdapter;
    AdvertsContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        ButterKnife.bind(AdvertsActivity.this);
        mAccount = TakeStockAccount.get(AdvertsActivity.this);
        Category category = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        Subcategory subcategory = getIntent().getParcelableExtra(EXTRA_SUBCATEGORY);
        setUpToolbar(category, subcategory);
        setUpRefreshLayout();
        setUpRecyclerView();
        AdvertFilter filter = new AdvertFilter.Builder()
                .setCategoryId(category.getId())
                .setSubcategoryId(subcategory.getId())
                .create();
        createPresenter(filter);
    }

    private void setUpToolbar(Category category, Subcategory subcategory) {
        Toolbar toolbar = ButterKnife.findById(AdvertsActivity.this, R.id.toolbar);
        toolbar.setTitle(category.getName());
        toolbar.setSubtitle(subcategory.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.refreshAdverts();
            }
        });
    }

    boolean isLoading;

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(AdvertsActivity.this, R.id.recycler_view);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_grid_space_4dp), 2);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int[] pastVisibleItems = layoutManager.findFirstVisibleItemPositions(new int[2]);
                if (isLoading) return;
                if ((visibleItemCount + pastVisibleItems[1]) >= totalItemCount) {
                    isLoading = true;
                    mPresenter.loadAdverts();
                }
            }
        });
        setUpAdvertAdapter(recyclerView);
    }

    private void setUpAdvertAdapter(RecyclerView recyclerView) {
        mAdvertsAdapter = new AdvertsAdapter(recyclerView.getContext());
        mAdvertsAdapter.setOnItemClickListener(new AdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Advert advert) {
                Timber.d(advert.toString());
            }
        });
        mAdvertsAdapter.setOnWatchingChangedListener(new AdvertsAdapter.OnWatchingChangedListener() {
            @Override public void onWatchingChanged(Advert advert, boolean isWatched) {
                mAdvertsAdapter.startAdvertProcessing(advert);
                if (mAccount.lacksAccount()) {
                    mAdvertsAdapter.stopAdvertProcessing(advert.getId());
                    startEntryActivity();
                } else {
                    mPresenter.addOrRemoveWatchingAdvert(advert.getId());
                }
            }
        });
        recyclerView.setAdapter(mAdvertsAdapter);
    }

    private void startEntryActivity() {
        startActivity(EntryActivity.getStartIntent(AdvertsActivity.this));
    }

    private void createPresenter(AdvertFilter filter) {
        new AdvertsPresenter(Injection.provideDataRepository(AdvertsActivity.this), AdvertsActivity.this, filter);
    }

    @Override public void setPresenter(@NonNull AdvertsContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshAdverts();
    }

    @Override public void showRefreshedAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.refreshAdverts(adverts);
    }

    @Override public void showLoadedAdvertsInView(List<Advert> adverts) {
        isLoading = false;
        mAdvertsAdapter.addAdverts(adverts);
    }

    @Override public void showAdvertAddedToWatching(int advertId) {
        stopAdvertProcessing(advertId);
    }

    @Override public void showAdvertRemovedFromWatching(int advertId) {
        stopAdvertProcessing(advertId);
    }

    private void stopAdvertProcessing(int advertId) {
        Advert advert = mAdvertsAdapter.getAdvertInProcessing(advertId);
        if (advert != null) {
            mAdvertsAdapter.stopAdvertProcessing(advertId);
        }
    }

    @Override public void showAdvertWatchingError(int advertId) {
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

    @Override public void setLoadingProgressIndicator(boolean isActive) {
        mAdvertsAdapter.setLoadingProgressEnable(isActive);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
