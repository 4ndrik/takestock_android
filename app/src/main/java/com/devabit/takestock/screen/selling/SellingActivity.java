package com.devabit.takestock.screen.selling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
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
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.advert.edit.AdvertEditActivity;
import com.devabit.takestock.screen.questions.QuestionsActivity;
import com.devabit.takestock.screen.offers.OffersActivity;
import com.devabit.takestock.screen.selling.adapters.SellingAdvertsAdapter;
import com.devabit.takestock.widget.ListSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class SellingActivity extends AppCompatActivity implements SellingContract.View {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SellingActivity.class);
    }

    @BindView(R.id.content_activity_selling) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private SellingContract.Presenter mPresenter;
    private SellingAdvertsAdapter mAdvertsAdapter;

    private boolean mAreAdvertsLoading;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        ButterKnife.bind(SellingActivity.this);
        setUpToolbar();
        setUpRecyclerView();
        setUpRefreshLayout();
        createPresenter();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(SellingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(SellingActivity.this, R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SellingActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0 || mAreAdvertsLoading) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mAreAdvertsLoading = true;
                    mPresenter.loadAdverts();
                }
            }
        });
        ListSpacingItemDecoration itemDecoration = new ListSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        mAdvertsAdapter = new SellingAdvertsAdapter(SellingActivity.this);
        mAdvertsAdapter.setOnItemClickListener(new SellingAdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClicked(Advert advert) {
                startAdvertDetailActivity(advert);
            }
        });
        mAdvertsAdapter.setOnMenuItemClickListener(mMenuItemClickListener);
        recyclerView.setAdapter(mAdvertsAdapter);
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.refreshAdverts();
            }
        });
    }

    private void createPresenter() {
        new SellingPresenter(getUserId(), Injection.provideDataRepository(SellingActivity.this), SellingActivity.this);
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshAdverts();
    }

    private final SellingAdvertsAdapter.OnMenuItemClickListener mMenuItemClickListener
            = new SellingAdvertsAdapter.OnMenuItemClickListener() {
        @Override public void manageOffers(Advert advert) {
            startOffersActivity(advert);
        }

        @Override public void viewQuestions(Advert advert) {
            startQuestionsActivity(advert);
        }

        @Override public void editAdvert(Advert advert) {
            startAdvertEditActivity(advert);
        }
    };

    private void startOffersActivity(Advert advert) {
        startActivity(OffersActivity.getStartIntent(SellingActivity.this, advert));
    }

    private void startQuestionsActivity(Advert advert) {
        startActivity(QuestionsActivity.getStartIntent(SellingActivity.this, advert.getId()));
    }

    private void startAdvertDetailActivity(Advert advert) {
        startActivity(AdvertDetailActivity.getStartIntent(SellingActivity.this, advert));
    }

    private void startAdvertEditActivity(Advert advert) {
        startActivity(AdvertEditActivity.getStartIntent(SellingActivity.this, advert));
    }

    private int getUserId() {
        return TakeStockAccount.get(SellingActivity.this).getUserId();
    }

    @Override public void showRefreshedAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.refreshAdverts(adverts);
    }

    @Override public void showLoadedAdvertsInView(List<Advert> adverts) {
        mAdvertsAdapter.addAdverts(adverts);
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
        mAdvertsAdapter.setLoadingProgress(isActive);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }
}
