package com.devabit.takestock.screen.adverts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.advert.active.AdvertActiveActivity;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.adverts.adapter.AdvertsAdapter;
import com.devabit.takestock.screen.category.CategoriesActivity;
import com.devabit.takestock.screen.dialog.emailConfirmation.EmailConfirmationDialog;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.widget.GridSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public class AdvertsActivity extends AppCompatActivity implements AdvertsContract.View {

    private static final String ACTION_SEARCHING = "ACTION_SEARCHING";
    private static final String ACTION_BROWSING = "ACTION_BROWSING";

    private static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    private static final String EXTRA_SUBCATEGORY = "EXTRA_SUBCATEGORY";
    private static final String EXTRA_QUERY = "EXTRA_QUERY";

    private static final int RC_ADVERT_ACTIVE = 102;
    private static final int RC_ENTRY = 103;

    public static Intent getSearchingStartIntent(Context context, String query) {
        Intent starter = new Intent(context, AdvertsActivity.class);
        starter.setAction(ACTION_SEARCHING);
        starter.putExtra(EXTRA_QUERY, query);
        starter.putExtra(EXTRA_CATEGORY, Category.ALL);
        return starter;
    }

    public static Intent getBrowsingStartIntent(Context context, Category category, Subcategory subcategory) {
        Intent starter = new Intent(context, AdvertsActivity.class);
        starter.setAction(ACTION_BROWSING);
        starter.putExtra(EXTRA_CATEGORY, category);
        starter.putExtra(EXTRA_SUBCATEGORY, subcategory);
        return starter;
    }

    private static final int RC_ADVERT_DETAIL = 101;

    @BindView(R.id.content) protected ViewGroup mContent;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.count_text_view) protected TextView mCountTextView;

    TakeStockAccount mAccount;
    @Nullable User mAccountUser;
    Category mCategory;
    Subcategory mSubcategory;
    String mQuery;
    AdvertsAdapter mAdvertsAdapter;
    AdvertsContract.Presenter mPresenter;

    int mTotalAdvertsCount;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        ButterKnife.bind(AdvertsActivity.this);
        mAccount = TakeStockAccount.get(AdvertsActivity.this);
        mAccountUser = mAccount.getUser();
        initDataFromIntent(getIntent());
        setUpToolbar();
        setUpRefreshLayout();
        setUpRecyclerView();
        createPresenter(createFilter());
    }

    private void initDataFromIntent(Intent intent) {
        mCategory = intent.getParcelableExtra(EXTRA_CATEGORY);
        mSubcategory = intent.getParcelableExtra(EXTRA_SUBCATEGORY);
        if (intent.hasExtra(EXTRA_QUERY)) {
            mQuery = intent.getStringExtra(EXTRA_QUERY);
        }
    }

    private void setUpToolbar() {
        if (TextUtils.isEmpty(mQuery)) {
            mToolbar.setTitle(mCategory.getName());
        } else {
            mToolbar.setTitle(getString(R.string.adverts_query_toolbar_title, mQuery, mCategory.getName()));
        }
        if (mSubcategory != null) {
            mToolbar.setSubtitle(mSubcategory.getName());
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.refreshAdverts();
            }
        });
    }

    boolean isLoading;

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(AdvertsActivity.this, R.id.recycler_view);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_grid_space_4dp), 2);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] pastVisibleItems = layoutManager.findFirstVisibleItemPositions(new int[2]);
                setAdvertsCount(pastVisibleItems[0]);
                if (dy <= 0 || isLoading) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visibleItemCount + pastVisibleItems[0]) >= totalItemCount) {
                    isLoading = true;
                    mPresenter.loadAdverts();
                }
            }
        });
        setUpAdvertAdapter(recyclerView);
    }

    private void setUpAdvertAdapter(RecyclerView recyclerView) {
        mAdvertsAdapter = new AdvertsAdapter(recyclerView.getContext(), mAccountUser);
        mAdvertsAdapter.setOnItemClickListener(new AdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Advert advert) {
                if (mAccountUser == null) {
                    displaySignInDialog();
                } else if (!mAccountUser.isVerified()) {
                    displayEmailConfirmationDialog();
                } else if (mAccountUser.getId() == advert.getUser().getId()) {
                    startAdvertSellingActivity(advert);
                } else {
                    startAdvertDetailActivity(advert);
                }
            }
        });

        mAdvertsAdapter.setOnWatchingChangedListener(new AdvertsAdapter.OnWatchingChangedListener() {
            @Override public void onWatchingChanged(Advert advert, boolean isWatched) {
                mAdvertsAdapter.startAdvertProcessing(advert);
                if (mAccount.lacksAccount()) {
                    mAdvertsAdapter.stopAdvertProcessing(advert.getId());
                    startEntryActivity();
                } else {
                    mPresenter.addOrRemoveWatchingAdvert(advert, mAccount.getId());
                }
            }
        });

        recyclerView.setAdapter(mAdvertsAdapter);
    }

    private void displaySignInDialog() {
        new AlertDialog.Builder(AdvertsActivity.this)
                .setMessage("Sign in to see details")
                .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void displayEmailConfirmationDialog() {
        EmailConfirmationDialog dialog = EmailConfirmationDialog.newInstance(mAccount.getEmail());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void startAdvertSellingActivity(Advert advert) {
        startActivityForResult(AdvertActiveActivity.getStartIntent(AdvertsActivity.this, advert), RC_ADVERT_ACTIVE);
    }

    private void startAdvertDetailActivity(Advert advert) {
        startActivityForResult(AdvertDetailActivity.getStartIntent(AdvertsActivity.this, advert.getId()), RC_ADVERT_DETAIL);
    }

    private void startEntryActivity() {
        startActivityForResult(EntryActivity.getStartIntent(AdvertsActivity.this), RC_ENTRY);
    }

    private void createPresenter(AdvertFilter filter) {
        new AdvertsPresenter(Injection.provideDataRepository(AdvertsActivity.this), AdvertsActivity.this, filter);
    }

    @Override public void setPresenter(@NonNull AdvertsContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshAdverts();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == RC_ADVERT_DETAIL || requestCode == RC_ADVERT_ACTIVE)
                && resultCode == RESULT_OK) {
            Advert advert = data.getParcelableExtra(getString(R.string.extra_advert));
            mAdvertsAdapter.refreshAdvert(advert);
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initDataFromIntent(intent);
        setUpToolbar();
        loadAdvertsWithFilter(createFilter());
    }

    @Override public void showRefreshedAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.refreshAdverts(adverts);
    }

    @Override public void showLoadedAdvertsInView(List<Advert> adverts) {
        isLoading = false;
        mAdvertsAdapter.addAdverts(adverts);
    }

    @Override public void showTotalAdvertsCountInView(int count) {
        mTotalAdvertsCount = count;
        setAdvertsCount(0);
    }

    void setAdvertsCount(int count) {
        String visibleCountText = String.valueOf(count);
        String countText = getString(R.string.adverts_count, visibleCountText, mTotalAdvertsCount);
        SpannableString spString = new SpannableString(countText);
        spString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, visibleCountText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCountTextView.setText(spString);
    }

    @Override public void showAdvertAddedToWatching(Advert advert) {
        stopAdvertProcessing(advert.getId());
        mAdvertsAdapter.refreshAdvert(advert);
    }

    @Override public void showAdvertRemovedFromWatching(Advert advert) {
        stopAdvertProcessing(advert.getId());
        mAdvertsAdapter.refreshAdvert(advert);
    }

    private void stopAdvertProcessing(int advertId) {
        Advert advert = mAdvertsAdapter.getAdvertInProcessing(advertId);
        if (advert != null) {
            mAdvertsAdapter.stopAdvertProcessing(advertId);
        }
    }

    @Override public void showAdvertWatchingError(Advert advert) {
        mAdvertsAdapter.stopAdvertProcessing(advert.getId());
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

    @Override public void setLoadingProgressIndicator(boolean isActive) {
        mAdvertsAdapter.setLoadingProgressEnable(isActive);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @OnClick(R.id.categories_button)
    protected void onCategoriesButtonClick() {
        startActivity(CategoriesActivity.getStartIntent(AdvertsActivity.this));
    }

    int mSortOrder = AdvertFilter.ORDER_CREATED_AT_DESCENDING;
    int mSortOrderCheckedItem = 1;

    private final static SparseIntArray SORT_VALUES = new SparseIntArray();

    static {
        SORT_VALUES.append(R.string.adverts_sort_created_at, AdvertFilter.ORDER_CREATED_AT);
        SORT_VALUES.append(R.string.adverts_sort_created_at_descending, AdvertFilter.ORDER_CREATED_AT_DESCENDING);
        SORT_VALUES.append(R.string.adverts_sort_expires_at, AdvertFilter.ORDER_EXPIRES_AT);
        SORT_VALUES.append(R.string.adverts_sort_expires_at_descending, AdvertFilter.ORDER_EXPIRES_AT_DESCENDING);
        SORT_VALUES.append(R.string.adverts_sort_guide_price, AdvertFilter.ORDER_GUIDE_PRICE);
        SORT_VALUES.append(R.string.adverts_sort_guide_price_descending, AdvertFilter.ORDER_GUIDE_PRICE_DESCENDING);
    }

    @OnClick(R.id.sort_button)
    protected void onSortButtonClick() {

        final CharSequence[] sortNames = new CharSequence[SORT_VALUES.size()];
        for (int i = 0; i < SORT_VALUES.size(); i++) {
            sortNames[i] = getString(SORT_VALUES.keyAt(i));
        }

        new AlertDialog.Builder(AdvertsActivity.this)
                .setNegativeButton(R.string.adverts_cancel, null)
                .setSingleChoiceItems(sortNames, mSortOrderCheckedItem, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mSortOrderCheckedItem = which;
                        int key = SORT_VALUES.keyAt(mSortOrderCheckedItem);
                        mSortOrder = SORT_VALUES.get(key);
                        loadAdvertsWithFilter(createFilter());
                    }
                })
                .show();
    }

    private void loadAdvertsWithFilter(AdvertFilter filter) {
        mPresenter.loadAdvertsWithFilter(filter);
    }

    private AdvertFilter createFilter() {
        return new AdvertFilter.Builder()
                .setCategory(mCategory)
                .setSubcategory(mSubcategory)
                .setQuery(mQuery)
                .setOrder(mSortOrder)
                .create();
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
