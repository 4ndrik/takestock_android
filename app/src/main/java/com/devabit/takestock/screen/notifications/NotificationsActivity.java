package com.devabit.takestock.screen.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.ui.decoration.DividerItemDecoration;
import com.devabit.takestock.ui.decoration.RemovingItemDecoration;
import com.devabit.takestock.ui.widget.SwipeMenuRecyclerView;
import com.devabit.takestock.utils.NotificationFactory;

import java.util.List;

/**
 * Created by Victor Artemyev on 20/10/2016.
 */

public class NotificationsActivity extends AppCompatActivity implements NotificationsContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, NotificationsActivity.class);
    }

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mRefreshLayout;

    NotificationsAdapter mNotificationsAdapter;
    NotificationsContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(NotificationsActivity.this);

        setUpToolbar();
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(NotificationsActivity.this, R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpSwipeRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.jam);
        mRefreshLayout.setEnabled(false);
    }

    private void setUpRecyclerView() {
        SwipeMenuRecyclerView recyclerView = ButterKnife.findById(NotificationsActivity.this, R.id.recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider_grey300));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(new RemovingItemDecoration());
        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        setUpNotificationAdapter(recyclerView);
    }

    private void setUpNotificationAdapter(RecyclerView recyclerView) {
        mNotificationsAdapter = new NotificationsAdapter(NotificationsActivity.this);
        mNotificationsAdapter.setOnItemClickListener(new NotificationsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Notification notification) {
                startActivity(NotificationFactory.getStartIntent(NotificationsActivity.this, notification));
            }

            @Override public void onItemDelete(Notification notification) {
                mPresenter.removeNotification(notification);
            }
        });
        recyclerView.setAdapter(mNotificationsAdapter);
    }

    private void createPresenter() {
        new NotificationsPresenter(Injection.provideDataRepository(NotificationsActivity.this), NotificationsActivity.this);
    }

    @Override public void setPresenter(@NonNull NotificationsContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadNotifications();
    }

    @Override public void showNotificationsInView(List<Notification> notifications) {
        mNotificationsAdapter.refreshNotifications(notifications);
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void showNotificationRemovedInView(Notification notification) {
        mNotificationsAdapter.removeNotification(notification);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
