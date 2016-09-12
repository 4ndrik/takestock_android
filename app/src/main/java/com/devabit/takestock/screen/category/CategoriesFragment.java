package com.devabit.takestock.screen.category;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.screen.category.adapter.CategoriesAdapter;
import com.devabit.takestock.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public class CategoriesFragment extends Fragment implements CategoryContract.View {

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    private CategoryContract.Presenter mPresenter;
    private CategoriesAdapter mCategoriesAdapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        Context context = view.getContext();
        new CategoryPresenter(Injection.provideDataRepository(context), CategoriesFragment.this);
        RecyclerView recyclerView = (RecyclerView) view;
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.divider_grey300));
        recyclerView.addItemDecoration(itemDecoration);
        mCategoriesAdapter = new CategoriesAdapter(recyclerView.getContext());
        recyclerView.setAdapter(mCategoriesAdapter);
        mCategoriesAdapter.setOnCategorySelectedListener(new CategoriesAdapter.OnCategorySelectedListener() {
            @Override public void onCategorySelected(Category category) {
                ((CategoriesActivity)getActivity()).setSelectedCategory(category);
            }
        });

        mPresenter.fetchCategories();
    }

    @Override public void setPresenter(@NonNull CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override public void showCategoriesInView(List<Category> categories) {
        mCategoriesAdapter.setCategories(categories);
    }

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showUnknownError() {

    }

    @Override public void setProgressIndicator(boolean isActive) {

    }

    @Override public void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
