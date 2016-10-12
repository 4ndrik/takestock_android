package com.devabit.takestock.screen.selling.fragment.activeAdverts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.devabit.takestock.Injection;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.advert.selling.AdvertSellingActivity;
import com.devabit.takestock.screen.selling.SellingContract;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsAdapter;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsFragment;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

public class ActiveAdvertsAdvertsFragment extends BaseAdvertsFragment {

    public static ActiveAdvertsAdvertsFragment newInstance() {
        return new ActiveAdvertsAdvertsFragment();
    }

    @Override protected void setUpAdapter(@NonNull RecyclerView recyclerView) {
        mAdvertAdapter = new ActiveAdvertsAdapter(recyclerView.getContext());
        mAdvertAdapter.setOnItemClickListener(new BaseAdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClicked(Advert advert) {
                startActivity(AdvertSellingActivity.getStartIntent(getActivity(), advert));
            }
        });
        recyclerView.setAdapter(mAdvertAdapter);
    }

    @Override protected void createPresenter() {
        new ActiveAdvertsPresenter(getUserId(),
                Injection.provideDataRepository(getActivity()), ActiveAdvertsAdvertsFragment.this);
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshAdverts();
    }

    @Override public void onDisplay() {

    }

    @Override public void onHide() {

    }
}
