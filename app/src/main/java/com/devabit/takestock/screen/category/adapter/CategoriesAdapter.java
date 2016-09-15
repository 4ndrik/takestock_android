package com.devabit.takestock.screen.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Category> mCategories;

    public interface OnCategorySelectedListener {
        void onCategorySelected(Category category);
    }

    OnCategorySelectedListener mListener;

    public CategoriesAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mCategories = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindCategory(mCategories.get(position));
    }

    @Override public int getItemCount() {
        return mCategories.size();
    }

    public void setCategories(List<Category> categories) {
        mCategories.add(Category.ALL);
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener itemClickListener) {
        mListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mNameTextView;
        private Category mCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView;
            mNameTextView.setOnClickListener(ViewHolder.this);
        }

        void bindCategory(Category category) {
            mCategory = category;
            mNameTextView.setText(mCategory.getName());
        }

        @Override public void onClick(View v) {
            if (mListener != null) mListener.onCategorySelected(mCategory);
        }
    }
}
