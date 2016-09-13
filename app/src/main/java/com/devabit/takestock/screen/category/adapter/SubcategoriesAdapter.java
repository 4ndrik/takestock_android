package com.devabit.takestock.screen.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Subcategory;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<Subcategory> mSubcategories;

    public interface OnSubcategorySelectedListener {
        void onSubcategorySelected(Subcategory subcategory);
    }

    OnSubcategorySelectedListener mListener;

    public SubcategoriesAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_subcategory, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindSubcategory(mSubcategories.get(position));
    }

    @Override public int getItemCount() {
        return mSubcategories == null ? 0 : mSubcategories.size();
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        mSubcategories = subcategories;
        notifyDataSetChanged();
    }

    public void setOnSubcategorySelectedListener(OnSubcategorySelectedListener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mNameTextView;
        private Subcategory mSubcategory;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView;
            mNameTextView.setOnClickListener(ViewHolder.this);
        }

        void bindSubcategory(Subcategory subcategory) {
            mSubcategory = subcategory;
            mNameTextView.setText(mSubcategory.getName());
        }

        @Override public void onClick(View v) {
            if (mListener != null) mListener.onSubcategorySelected(mSubcategory);
        }
    }
}
