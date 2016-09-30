package com.devabit.takestock.screen.advert.selling.fragment.offers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.dialog.counterOffer.CounterOfferDialog;
import com.devabit.takestock.screen.dialog.rejectOffer.RejectOfferDialog;
import com.devabit.takestock.widget.ListSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 30/09/2016.
 */

public class OffersFragment extends Fragment implements OffersContract.View {

    private static final String ARG_ADVERT = "ARG_ADVERT";

    public static OffersFragment newInstance(Advert advert) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ADVERT, advert);
        OffersFragment fragment = new OffersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    SwipeRefreshLayout mSwipeRefreshLayout;

    Advert mAdvert;
    OffersContract.Presenter mPresenter;
    OffersAdapter mOffersAdapter;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdvert = getArguments().getParcelable(ARG_ADVERT);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        createPresenter(view);
    }

    private void setUpSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = ButterKnife.findById(view, R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.refreshOffers();
            }
        });
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        ListSpacingItemDecoration itemDecoration = new ListSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        mOffersAdapter = new OffersAdapter(view.getContext(), mAdvert.getPackagingName());
        mOffersAdapter.setOnStatusChangedListener(mStatusChangedListener);
        recyclerView.setAdapter(mOffersAdapter);
    }

    private final OffersAdapter.OnStatusChangedListener mStatusChangedListener
            = new OffersAdapter.OnStatusChangedListener() {
        @Override public void onAccepted(Offer offer) {
            Offer.Accept accept = new Offer.Accept.Builder()
                    .setOfferId(offer.getId())
                    .setStatus(Offer.Status.ACCEPTED)
                    .setFromSeller(true)
                    .create();
            mPresenter.acceptOffer(offer, accept);
        }

        @Override public void onCountered(Offer offer) {
            displayCounterOfferMakerDialog(offer);
        }

        @Override public void onRejected(Offer offer) {
            displayRejectOfferMakerDialog(offer);
        }
    };

    private void displayCounterOfferMakerDialog(final Offer offer) {
        CounterOfferDialog dialog = CounterOfferDialog.newInstance(mAdvert, offer, true);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnOfferCounteredListener(new CounterOfferDialog.OnOfferCounteredListener() {
            @Override public void onCountered(CounterOfferDialog dialog, Offer.Accept accept) {
                dialog.dismiss();
                mPresenter.acceptOffer(offer, accept);
            }
        });
    }

    private void displayRejectOfferMakerDialog(final Offer offer) {
        RejectOfferDialog dialog = RejectOfferDialog.newInstance(offer, true);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnRejectOfferListener(new RejectOfferDialog.OnRejectOfferListener() {
            @Override public void onOfferRejected(RejectOfferDialog dialog, Offer.Accept accept) {
                dialog.dismiss();
                mPresenter.acceptOffer(offer, accept);
            }
        });
    }

    private void createPresenter(View view) {
        new OffersPresenter(mAdvert.getId(), Injection.provideDataRepository(view.getContext()), OffersFragment.this);
    }

    @Override public void setPresenter(@NonNull OffersContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshOffers();
    }

    @Override public void showRefreshedOffersInView(List<Offer> offers) {
        mOffersAdapter.refreshOffers(offers);
    }

    @Override public void showLoadedOffersInView(List<Offer> offers) {

    }

    @Override public void showOfferAcceptedInView(Offer offer) {
        mOffersAdapter.refreshOffer(offer);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mSwipeRefreshLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setLoadingProgressIndicator(boolean isActive) {
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    @Override public void showUpdatedOfferInView(Offer offer) {

    }

    @Override public void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
