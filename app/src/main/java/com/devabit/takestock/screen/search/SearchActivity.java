package com.devabit.takestock.screen.search;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.category.CategoriesDialog;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.screen.search.adapter.SearchAdvertsAdapter;
import com.devabit.takestock.utils.FontCache;
import com.devabit.takestock.widgets.AdvertSortView;

import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 11/04/2016.
 */
public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    private static final String TAG = makeLogTag(SearchActivity.class);

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    private static final int REQUEST_CODE_LACK_ACCOUNT = 100;

    private static final int NO_USER = -1;

    @BindView(R.id.content_activity_search) protected View mContent;
    @BindView(R.id.result_count_text_view) protected TextView mResultCountTextView;
    @BindView(R.id.sort_view) protected AdvertSortView mSortView;
    @BindView(R.id.filter_button) protected Button mFilterButton;
    @BindView(R.id.sort_button) protected Button mSortButton;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view) protected RecyclerView mAdvertsRecyclerView;

    @BindViews({R.id.browse_categories_button, /*R.id.filter_button, R.id.newest_first_button*/})
    protected List<Button> mButtons;

    private SearchAdvertsAdapter mAdvertsAdapter;
    private SearchContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(SearchActivity.this);

        new SearchPresenter(
                Injection.provideDataRepository(SearchActivity.this), SearchActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        final Typeface mediumTypeface = FontCache.getTypeface(this, R.string.font_brandon_medium);

        setUpToolbar(boldTypeface);

        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(Button view, int index) {
                view.setTypeface(mediumTypeface);
            }
        });

        setUpAdvertsRecyclerView();

        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mAdvertsAdapter.clearAdverts();
                mPresenter.refreshAdverts();
            }
        });

        mSortView.setOnOrderListener(new AdvertSortView.OnOrderListener() {
            @Override public void onOrder(@AdvertFilter.Order int order) {
                toggleSortView();
                mAdvertsAdapter.clearAdverts();
                AdvertFilter filter = getAdvertFilter(order);
                mPresenter.fetchAdvertsPerFilter(filter);
            }
        });
    }

    @Override public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void setUpToolbar(Typeface boldTypeface) {
        Toolbar toolbar = ButterKnife.findById(SearchActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.search);
    }

    private void setUpAdvertsRecyclerView() {
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdvertsRecyclerView.setLayoutManager(gridLayoutManager);
        mAdvertsAdapter = new SearchAdvertsAdapter(SearchActivity.this);
        mAdvertsAdapter.setUserId(getUserId());
        mAdvertsAdapter.setOnEndPositionListener(new SearchAdvertsAdapter.OnEndPositionListener() {
            @Override public void onEndPosition(int position) {
                fetchAdverts();
            }
        });
        mAdvertsAdapter.setOnItemClickListener(new SearchAdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Advert advert) {
                startAdvertDetailActivity(advert);
            }
        });
        mAdvertsAdapter.setOnWatchedChangeListener(new SearchAdvertsAdapter.OnWatchedChangeListener() {
            @Override public void onWatchedChanged(Advert advert, boolean isWatched) {
                mAdvertsAdapter.startAdvertProcessing(advert);
                if (getUserId() == NO_USER) {
                    mAdvertsAdapter.stopAdvertProcessing(advert.getId());
                    startEntryActivity();
                } else {
                    mPresenter.addRemoveWatchingAdvert(advert.getId());
                }
            }
        });
        mAdvertsRecyclerView.setAdapter(mAdvertsAdapter);
    }

    private void startAdvertDetailActivity(Advert advert) {
        LOGD(TAG, "Advert: " + advert);
        startActivity(AdvertDetailActivity.getStartIntent(SearchActivity.this, advert));
    }

    private void startEntryActivity() {
        startActivityForResult(EntryActivity.getStartIntent(SearchActivity.this), REQUEST_CODE_LACK_ACCOUNT);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LACK_ACCOUNT && resultCode == RESULT_OK) {
            mAdvertsAdapter.setUserId(getUserId());
        }
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(SearchActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        if (accounts.length == 0) return NO_USER;
        String userId = accountManager.getUserData(accounts[0], getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    private AdvertFilter getAdvertFilter(@AdvertFilter.Order int order) {
        AdvertFilter filter = new AdvertFilter();
        filter.setOrder(order);
        return filter;
    }

    @Override protected void onResume() {
        super.onResume();
        fetchAdverts();
    }

    private void fetchAdverts() {
        mPresenter.fetchAdverts();
    }

    @Override public void showAdvertsCountInView(int count) {
        mResultCountTextView.setText(getString(R.string.count_results_for, count));
    }

    @Override public void showAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.addAdverts(adverts);
    }

    @Override public void showAdvertAddedToWatching(int advertId) {
        Advert advert = mAdvertsAdapter.getAdvertInProcessing(advertId);
        if (advert != null) {
            advert.addSubscriber(getUserId());
            mAdvertsAdapter.stopAdvertProcessing(advertId);
        }
    }

    @Override public void showAdvertRemovedFromWatching(int advertId) {
        Advert advert = mAdvertsAdapter.getAdvertInProcessing(advertId);
        if (advert != null) {
            advert.removeSubscriber(getUserId());
            mAdvertsAdapter.stopAdvertProcessing(advertId);
        }
    }

    @Override public void showAdvertWatchingError(int advertId) {
        mAdvertsAdapter.stopAdvertProcessing(advertId);
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

    @OnClick(R.id.browse_categories_button)
    protected void onBrowseCategoriesButtonClick() {
        displayCategoriesDialog();
    }

    private void displayCategoriesDialog() {
        CategoriesDialog dialog = CategoriesDialog.newInstance();
        dialog.show(getFragmentManager(), dialog.getClass().getCanonicalName());
    }

    @OnClick(R.id.sort_button)
    protected void onSortButtonClick() {
        toggleSortView();
    }

    private void toggleSortView() {
        if (mSortView.getVisibility() == View.VISIBLE) {
            mSortButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_down_lime_8dp, 0);
            slideUpView(mSortView);
            mSortView.setVisibility(View.GONE);
        } else {
            mSortButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_up_lime_8dp, 0);
            mSortView.setVisibility(View.VISIBLE);
            slideDownView(mSortView);
        }
    }

    private void slideDownView(View view) {
        Animation animation = AnimationUtils.loadAnimation(SearchActivity.this, R.anim.slide_down);
        view.startAnimation(animation);
    }

    private void slideUpView(View view) {
        Animation animation = AnimationUtils.loadAnimation(SearchActivity.this, R.anim.slide_up);
        view.startAnimation(animation);
    }

    @Override public void onBackPressed() {
        if (mSortView.getVisibility() == View.VISIBLE) {
            mSortButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_down_lime_8dp, 0);
            slideUpView(mSortView);
            mSortView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mAdvertsAdapter.destroy();
    }
}
