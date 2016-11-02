package com.devabit.takestock.screen.userProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.ui.widget.ControllableAppBarLayout;
import com.devabit.takestock.ui.decoration.GridSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/10/2016.
 */

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View {

    private static final String EXTRA_USER = "EXTRA_USER";

    public static Intent getStartIntent(Context context, User user) {
        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(EXTRA_USER, user);
        return starter;
    }

    @BindView(R.id.content) ViewGroup mContent;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.user_image_view) ImageView mUserImageView;
    @BindView(R.id.user_name_text_view) TextView mUserNameTextView;
    @BindView(R.id.user_rating_bar) RatingBar mUserRatingBar;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mRefreshLayout;

    User mUser;
    UserAdvertListingAdapter mAdvertListingAdapter;
    UserProfileContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(UserProfileActivity.this);
        setUpToolbar();
        setUpRecyclerView();
        setUpAppBarLayout();
        setUpRefreshLayout();

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        bindUser(user);
        createPresenter(user);
    }

    private void setUpToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void bindUser(User user) {
        mUser = user;
        bindUserPhoto(user.getPhoto());
        mUserNameTextView.setText(user.getUserName());
        mUserRatingBar.setRating((float) user.getAvgRating());
    }

    private void bindUserPhoto(String photo) {
        if (photo.isEmpty()) return;
        Glide.with(mUserImageView.getContext())
                .load(photo)
                .error(R.drawable.ic_placeholder_user_96dp)
                .crossFade()
                .into(mUserImageView);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(UserProfileActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new GridLayoutManager(UserProfileActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_grid_space_4dp), 2);
        recyclerView.addItemDecoration(itemDecoration);
        setUpListingAdapter(recyclerView);
    }

    private void setUpListingAdapter(RecyclerView recyclerView) {
        mAdvertListingAdapter = new UserAdvertListingAdapter(recyclerView.getContext());
        mAdvertListingAdapter.setOnItemClickListener(new UserAdvertListingAdapter.OnItemClickListener() {
            @Override public void onItemClick(Advert advert) {
                startActivity(AdvertDetailActivity.getStartIntent(UserProfileActivity.this, advert.getId()));
            }
        });
        recyclerView.setAdapter(mAdvertListingAdapter);
    }

    private void setUpAppBarLayout() {
        ControllableAppBarLayout appBarLayout = ButterKnife.findById(UserProfileActivity.this, R.id.appbar_layout);
        appBarLayout.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override public void onStateChange(int toolbarChange) {
                switch (toolbarChange) {
                    case ControllableAppBarLayout.State.COLLAPSED:
                        mToolbar.setTitle(mUser.getUserName());
                        mToolbar.setSubtitle(getResources().getQuantityString(R.plurals.adverts, mAdvertListingAdapter.getItemCount(), mAdvertListingAdapter.getItemCount()));
                        break;
                    case ControllableAppBarLayout.State.EXPANDED:
                    case ControllableAppBarLayout.State.IDLE: // Just fired once between switching states
                        mToolbar.setTitle("");
                        mToolbar.setSubtitle("");
                        break;
                }
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setEnabled(false);
    }

    private void createPresenter(User user) {
        new UserProfilePresenter(user,
                Injection.provideDataRepository(UserProfileActivity.this), UserProfileActivity.this);
    }

    @Override public void setPresenter(@NonNull UserProfileContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadAdvert();
    }

    @Override public void showAdvertsInView(List<Advert> adverts) {
        mAdvertListingAdapter.refreshAdverts(adverts);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
