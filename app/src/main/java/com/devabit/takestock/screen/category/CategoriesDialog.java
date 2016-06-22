package com.devabit.takestock.screen.category;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Category;
import com.devabit.takestock.screen.category.adapter.CategoriesAdapter;
import com.devabit.takestock.screen.search.SearchActivity;

import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class CategoriesDialog extends DialogFragment implements CategoryContract.View {

    private static final String TAG = makeLogTag(CategoriesDialog.class);

    public static CategoriesDialog newInstance() {
        CategoriesDialog dialog = new CategoriesDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return dialog;
    }

    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private Unbinder mUnbinder;
    private CategoryContract.Presenter mPresenter;
    private CategoriesAdapter mCategoriesAdapter;
    private List<Category> mCategories;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CategoryPresenter(Injection.provideDataRepository(getActivity()), CategoriesDialog.this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_categories, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(CategoriesDialog.this, view);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                fetchCategories();
            }
        });
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_view);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mCategoriesAdapter = new CategoriesAdapter(getActivity());
        mCategoriesAdapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
            @Override public void onItemClick(View itemView, int position) {
                Category category = mCategories.get(position);
                LOGD(TAG, category.toString());
                dismiss();
                startSearchActivity();
            }
        });
        recyclerView.setAdapter(mCategoriesAdapter);
    }

    private void startSearchActivity() {
        startActivity(SearchActivity.getStartIntent(getActivity()));
    }

    @Override public void onResume() {
        super.onResume();
        fetchCategories();
    }

    private void fetchCategories() {
        mPresenter.fetchCategories();
    }

    @Override public void showCategoriesInView(List<Category> categories) {
        mCategories = categories;
        mCategoriesAdapter.setCategories(mCategories);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mRefreshLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void setPresenter(@NonNull CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.cancel_button)
    protected void onCancelButtonClick() {
        dismiss();
    }

    @Override public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
