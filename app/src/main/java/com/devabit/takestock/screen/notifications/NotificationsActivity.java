package com.devabit.takestock.screen.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.devabit.takestock.screen.advert.active.AdvertActiveActivity;
import com.devabit.takestock.widget.ListVerticalSpacingItemDecoration;

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
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setEnabled(false);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(NotificationsActivity.this, R.id.recycler_view);
        ListVerticalSpacingItemDecoration itemDecoration = new ListVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        setUpNotificationAdapter(recyclerView);
    }

    private void setUpNotificationAdapter(RecyclerView recyclerView) {
        mNotificationsAdapter = new NotificationsAdapter(NotificationsActivity.this);
        mNotificationsAdapter.setOnItemClickListener(new NotificationsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Notification notification) {
                startActivity(AdvertActiveActivity.getStartIntent(NotificationsActivity.this, notification));
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

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
