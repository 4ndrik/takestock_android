package com.devabit.takestock.screen.selling.fragment.expiredAdverts;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.screen.selling.SellingContract;
import com.devabit.takestock.screen.selling.SellingPresenter;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

final class ExpiredAdvertsPresenter extends SellingPresenter {

    ExpiredAdvertsPresenter(int userId, @NonNull DataRepository dataRepository, @NonNull SellingContract.View sellingView) {
        super(userId, dataRepository, sellingView);
    }

    @Override protected AdvertFilter createFilter() {
        return new AdvertFilter.Builder()
                .setAuthorId(mUserId)
                .setOrder(AdvertFilter.ORDER_UPDATED_AT_DESCENDING)
                .setAdditions(AdvertFilter.Addition.POSTED, AdvertFilter.Addition.HOLD_ON)
                .setAdditionalFilter(AdvertFilter.AdditionalFilter.EXPIRED)
                .create();
    }
}
