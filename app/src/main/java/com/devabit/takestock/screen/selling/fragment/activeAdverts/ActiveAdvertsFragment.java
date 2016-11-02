package com.devabit.takestock.screen.selling.fragment.activeAdverts;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.advert.selling.AdvertSellingActivity;
import com.devabit.takestock.screen.selling.SellingContract;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsAdapter;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsFragment;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

public class ActiveAdvertsFragment extends BaseAdvertsFragment {

    private static final int RC_ADVERT_ACTIVE = 3001;

    public static ActiveAdvertsFragment newInstance() {
        return new ActiveAdvertsFragment();
    }

    @Override protected void setUpAdapter(@NonNull RecyclerView recyclerView) {
        mAdvertAdapter = new ActiveAdvertsAdapter(recyclerView.getContext());
        mAdvertAdapter.setOnItemClickListener(new BaseAdvertsAdapter.OnItemClickListener() {
            @Override public void onItemClicked(Advert advert) {
                startActivityForResult(AdvertSellingActivity.getStartIntent(getActivity(), advert.getId()), RC_ADVERT_ACTIVE);
            }
        });
        recyclerView.setAdapter(mAdvertAdapter);
    }

    @Override protected void createPresenter() {
        new ActiveAdvertsPresenter(getUserId(),
                Injection.provideDataRepository(getActivity()), ActiveAdvertsFragment.this);
    }

    @Override public void setPresenter(@NonNull SellingContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshAdverts();
    }

    @Override public void onDisplay() {

    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADVERT_ACTIVE && resultCode == Activity.RESULT_OK) {
            Advert advert = data.getParcelableExtra(getString(R.string.extra_advert));
            mAdvertAdapter.refreshAdvert(advert);
        }
    }

    @Override public void onHide() {

    }
}
