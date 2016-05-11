package com.devabit.takestock.ui.search;

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
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.ui.advertDetail.AdvertDetailActivity;
import com.devabit.takestock.ui.search.adapter.AdvertAdapter;
import com.devabit.takestock.util.FontCache;

import java.util.List;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 11/04/2016.
 */
public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    private static final String TAG = makeLogTag(SearchActivity.class);

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, SearchActivity.class);
        return starter;
    }

    @BindView(R.id.content_activity_search) protected View mContent;
    @BindView(R.id.result_count_text_view) protected TextView mResultCountTextView;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;

    @BindViews({R.id.browse_categories_button, /*R.id.filter_button, R.id.newest_first_button*/})
    protected List<Button> mButtons;

    private AdvertAdapter mAdvertAdapter;
    private List<Advert> mAdverts;
    private SearchContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(SearchActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        final Typeface mediumTypeface = FontCache.getTypeface(this, R.string.font_brandon_medium);

        Toolbar toolbar = ButterKnife.findById(SearchActivity.this, R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.search);

        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(Button view, int index) {
                view.setTypeface(mediumTypeface);
            }
        });

        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                fetchAdverts();
            }
        });

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdvertAdapter = new AdvertAdapter(SearchActivity.this);
        mAdvertAdapter.setOnItemClickListener(new AdvertAdapter.OnItemClickListener() {
            @Override public void onItemClick(View itemView, int position) {
                Advert advert = mAdverts.get(position);
                LOGD(TAG, "Advert: " + advert);
                startActivity(AdvertDetailActivity.getStartIntent(SearchActivity.this, advert));
            }
        });
        mRecyclerView.setAdapter(mAdvertAdapter);

        new SearchPresenter(
                Injection.provideDataRepository(SearchActivity.this), SearchActivity.this);
    }

    @Override protected void onResume() {
        super.onResume();
        fetchAdverts();
    }

    private void fetchAdverts() {
        mPresenter.fetchAdverts();
    }

    @Override public void showAdvertsInView(List<Advert> adverts) {
        mAdverts = adverts;
        mResultCountTextView.setText(getString(R.string.count_results_for, mAdverts.size()));
        mAdvertAdapter.setAdverts(adverts);
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

    @Override public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }
}
