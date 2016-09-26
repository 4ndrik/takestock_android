package com.devabit.takestock.screen.category.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.screen.category.CategoriesActivity;
import com.devabit.takestock.screen.category.CategoriesContract;
import com.devabit.takestock.screen.category.CategoriesPresenter;
import com.devabit.takestock.screen.category.adapter.CategoriesAdapter;
import com.devabit.takestock.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public class CategoriesFragment extends Fragment implements CategoriesContract.View {

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    private CategoriesContract.Presenter mPresenter;
    private CategoriesAdapter mCategoriesAdapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(CategoriesFragment.this, view);
        setUpRefreshLayout();
        setUpRecyclerView();
        createPresenter();
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.fetchCategories();
            }
        });
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(mRecyclerView.getContext(), R.drawable.divider_grey300));
        mRecyclerView.addItemDecoration(itemDecoration);
        mCategoriesAdapter = new CategoriesAdapter(mRecyclerView.getContext());
        mRecyclerView.setAdapter(mCategoriesAdapter);
        mCategoriesAdapter.setOnCategorySelectedListener(new CategoriesAdapter.OnCategorySelectedListener() {
            @Override public void onCategorySelected(Category category) {
                ((CategoriesActivity) getActivity()).setSelectedCategory(category);
            }
        });
    }

    private void createPresenter() {
        new CategoriesPresenter(Injection.provideDataRepository(getActivity()), CategoriesFragment.this);
    }

    @Override public void setPresenter(@NonNull CategoriesContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchCategories();
    }

    @Override public void showCategoriesInView(List<Category> categories) {
        mCategoriesAdapter.setCategories(categories);
    }

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showUnknownError() {

    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
