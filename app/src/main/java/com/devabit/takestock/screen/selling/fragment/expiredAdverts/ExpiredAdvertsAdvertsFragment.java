package com.devabit.takestock.screen.selling.fragment.expiredAdverts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.devabit.takestock.Injection;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.advert.editor.AdvertEditorActivity;
import com.devabit.takestock.screen.selling.SellingContract;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsAdapter;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsFragment;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

public class ExpiredAdvertsAdvertsFragment extends BaseAdvertsFragment {

    private static final int RC_EDIT_ADVERT = 1004;

    public static ExpiredAdvertsAdvertsFragment newInstance() {
        return new ExpiredAdvertsAdvertsFragment();
    }

    @Override protected void setUpAdapter(@NonNull RecyclerView recyclerView) {
        mAdvertAdapter = new ExpiredAdvertsAdapter(recyclerView.getContext());
        mAdvertAdapter.setOnItemClickListener(new BaseAdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClicked(Advert advert) {
                startActivityForResult(AdvertEditorActivity.getStartIntent(getActivity(), advert), RC_EDIT_ADVERT);
            }
        });
        recyclerView.setAdapter(mAdvertAdapter);
    }

    @Override protected void createPresenter() {
        new ExpiredAdvertsPresenter(getUserId(),
                Injection.provideDataRepository(getActivity()), ExpiredAdvertsAdvertsFragment.this);
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override public void onDisplay() {
        if (mIsDisplayedAtFirst) return;
        if (mPresenter != null) {
            mIsDisplayedAtFirst = true;
            mPresenter.refreshAdverts();
        }
    }

    @Override public void onHide() {

    }
}
