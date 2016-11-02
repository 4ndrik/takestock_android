package com.devabit.takestock.screen.selling.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.selling.SellingContract;
import com.devabit.takestock.ui.decoration.ListVerticalSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

public abstract class BaseAdvertsFragment extends Fragment implements SellingContract.View {

    private static final int CONTENT_REFRESH_LIST = 0;
    private static final int CONTENT_NO_CONNECTION = 1;

    @BindView(R.id.view_switcher) ViewSwitcher mViewSwitcher;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    protected SellingContract.Presenter mPresenter;
    protected BaseAdvertsAdapter mAdvertAdapter;
    protected boolean mIsDisplayedAtFirst;
    protected boolean mAreAdvertsLoading;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_selling, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(BaseAdvertsFragment.this, view);
        setUpRefreshLayout();
        setUpRecyclerView();
        createPresenter();
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshAdverts();
            }
        });
    }

    protected void setUpRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0 || mAreAdvertsLoading) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mAreAdvertsLoading = true;
                    mPresenter.loadAdverts();
                }
            }
        });
        ListVerticalSpacingItemDecoration itemDecoration = new ListVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);
        setUpAdapter(mRecyclerView);
    }

    protected abstract void setUpAdapter(@NonNull RecyclerView recyclerView);

    protected abstract void createPresenter();

    protected int getUserId() {
        return TakeStockAccount.get(getActivity()).getId();
    }

    public abstract void onDisplay();

    @OnClick(R.id.retry_connection_button)
    void onRetryConnectionButtonClick() {
        mViewSwitcher.setDisplayedChild(CONTENT_REFRESH_LIST);
        refreshAdverts();
    }

    protected void refreshAdverts() {
        if (mPresenter != null) mPresenter.refreshAdverts();
    }

    @Override public void showRefreshedAdvertsInView(List<Advert> adverts) {
        mAdvertAdapter.refreshAdverts(adverts);
    }

    @Override public void showLoadedAdvertsInView(List<Advert> adverts) {
        mAreAdvertsLoading = false;
        mAdvertAdapter.addAdverts(adverts);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void setLoadingProgressIndicator(boolean isActive) {
        mAdvertAdapter.setLoadingProgress(isActive);
    }

    @Override public void showNetworkConnectionError() {
        mViewSwitcher.setDisplayedChild(CONTENT_NO_CONNECTION);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    protected void showSnack(@StringRes int resId) {
        Snackbar.make(mViewSwitcher, resId, Snackbar.LENGTH_SHORT).show();
    }

    public abstract void onHide();

    @Override public void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
