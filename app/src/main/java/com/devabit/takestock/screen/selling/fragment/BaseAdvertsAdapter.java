package com.devabit.takestock.screen.selling.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

public abstract class BaseAdvertsAdapter extends RecyclerView.Adapter<BaseAdvertsAdapter.ViewHolder> {

    protected final LayoutInflater mLayoutInflater;
    private final @LayoutRes int mItemLayoutResId;
    private final List<Advert> mAdverts;

    public interface OnItemClickListener {
        void onItemClicked(Advert advert);
    }

    private OnItemClickListener mItemClickListener;

    protected BaseAdvertsAdapter(Context context, @LayoutRes int itemLayoutResId) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayoutResId = itemLayoutResId;
        mAdverts = new ArrayList<>();
    }

    protected View inflateViewType(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == mItemLayoutResId) {
            ((ItemViewHolder)holder).bindAdvert(mAdverts.get(position));
        }
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public @LayoutRes int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        if (advert == null) return R.layout.item_progress;
        else return mItemLayoutResId;
    }

    public void refreshAdverts(List<Advert> adverts) {
        mAdverts.clear();
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void refreshAdvert(Advert advert) {
        int position = mAdverts.indexOf(advert);
        mAdverts.set(position, advert);
        notifyItemChanged(position);
    }

    void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    void setLoadingProgress(boolean active) {
        if (active) {
            mAdverts.add(null);
            notifyItemInserted(mAdverts.size());
        } else {
            mAdverts.remove(mAdverts.size() - 1);
            notifyItemRemoved(mAdverts.size());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    protected class ProgressViewHolder extends ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected abstract class ItemViewHolder extends ViewHolder {

        protected final Resources resources;
        protected Advert advert;

        protected ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ItemViewHolder.this, itemView);
            resources = itemView.getResources();
        }

        protected abstract void bindAdvert(Advert advert);

        protected abstract void bindPhoto(Photo photo);


        @OnClick(R.id.content)
        void onContentClick() {
            if(mItemClickListener != null) mItemClickListener.onItemClicked(this.advert);
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
