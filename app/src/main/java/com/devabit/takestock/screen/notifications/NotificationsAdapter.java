package com.devabit.takestock.screen.notifications;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Notification> mNotifications;

    NotificationsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mNotifications = new ArrayList<>();
    }

    public interface OnItemClickListener {
        void onItemClick(Notification notification);
    }

    private OnItemClickListener mOnItemClickListener;

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationViewHolder(inflateItemView(R.layout.item_notification, parent));
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        ((NotificationViewHolder) holder).bindNotification(notification);
    }

    @Override public int getItemCount() {
        return mNotifications.size();
    }

    void refreshNotifications(List<Notification> notifications) {
        mNotifications.clear();
        mNotifications.addAll(notifications);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class NotificationViewHolder extends ViewHolder {

        @BindView(R.id.title_text_view) TextView titleTextView;
        @BindView(R.id.body_text_view) TextView bodyTextView;

        Notification mNotification;

        NotificationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(mNotification);
                }
            });
            ButterKnife.bind(NotificationViewHolder.this, itemView);
        }

        void bindNotification(Notification notification) {
            mNotification = notification;
            titleTextView.setText(notification.getTitle());
            bodyTextView.setText(notification.getBody());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
