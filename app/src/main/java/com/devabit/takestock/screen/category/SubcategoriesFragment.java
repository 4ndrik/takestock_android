package com.devabit.takestock.screen.category;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.screen.category.adapter.SubcategoriesAdapter;
import com.devabit.takestock.widget.DividerItemDecoration;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public class SubcategoriesFragment extends Fragment {

    private static final String KEY_SUBCATEGORIES = "SUBCATEGORIES";

    public static SubcategoriesFragment newInstance(Subcategory[] subcategories) {
        Bundle args = new Bundle();
        args.putParcelableArray(KEY_SUBCATEGORIES, subcategories);
        SubcategoriesFragment fragment = new SubcategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SubcategoriesAdapter mSubcategoriesAdapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subcategories, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.divider_grey300));
        recyclerView.addItemDecoration(itemDecoration);
        mSubcategoriesAdapter = new SubcategoriesAdapter(context);
        Subcategory[] subcategories = (Subcategory[]) getArguments().getParcelableArray(KEY_SUBCATEGORIES);
        mSubcategoriesAdapter.setSubcategories(subcategories);
        recyclerView.setAdapter(mSubcategoriesAdapter);
        mSubcategoriesAdapter.setOnSubcategorySelectedListener(new SubcategoriesAdapter.OnSubcategorySelectedListener() {
            @Override public void onSubcategorySelected(Subcategory subcategory) {

            }
        });
    }
}
