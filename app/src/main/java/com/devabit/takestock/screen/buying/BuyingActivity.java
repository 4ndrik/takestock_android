package com.devabit.takestock.screen.buying;

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
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.buying.adapter.OfferAdvertPairsAdapter;
import com.devabit.takestock.widget.ListSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public class BuyingActivity extends AppCompatActivity implements BuyingContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BuyingActivity.class);
    }

    @BindView(R.id.content_activity_buying) protected ViewGroup mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private BuyingContract.Presenter mPresenter;

    private OfferAdvertPairsAdapter mOffersAdvertAdapter;

    private boolean mAreOffersLoading;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        ButterKnife.bind(BuyingActivity.this);
        setUpToolbar();
        setUpRefreshLayout();
        setUpRecyclerView();
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(BuyingActivity.this, R.id.toolbar);
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
                mPresenter.refreshOffers();
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(BuyingActivity.this, R.id.recycler_view);
        ListSpacingItemDecoration itemDecoration = new ListSpacingItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(BuyingActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0 || mAreOffersLoading) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mAreOffersLoading = true;
                    mPresenter.loadOffers();
                }
            }
        });
        mOffersAdvertAdapter = new OfferAdvertPairsAdapter(BuyingActivity.this);
        recyclerView.setAdapter(mOffersAdvertAdapter);
        mOffersAdvertAdapter.setOnItemClickListener(new OfferAdvertPairsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Pair<Offer, Advert> offerAdvertPair) {

            }
        });
    }

    private void createPresenter() {
        new BuyingPresenter(
                Injection.provideDataRepository(BuyingActivity.this), BuyingActivity.this);
    }

    @Override public void setPresenter(@NonNull BuyingContract.Presenter presenter) {
        mPresenter = presenter;
        presenter.refreshOffers();
    }

    @Override public void showRefreshedOfferAdvertPairsInView(List<Pair<Offer, Advert>> pairs) {
        mOffersAdvertAdapter.refreshPairs(pairs);
    }

    @Override public void showLoadedOfferAdvertPairsInView(List<Pair<Offer, Advert>> pairs) {
        mAreOffersLoading = false;
        mOffersAdvertAdapter.addPairs(pairs);
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
        mOffersAdvertAdapter.setLoadingProgressEnable(isActive);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
