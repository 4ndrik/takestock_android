package com.devabit.takestock.screens.buying;

import android.util.SparseArray;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.OfferStatus;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

import java.util.Map;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public interface BuyingContract {

    interface View extends BaseView<Presenter> {

        void showOfferStatusesInView(SparseArray<OfferStatus> statuses);

        void showOfferAdvertPairsInView(Map<Offer, Advert> offerAdvertMap);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchOffersByUserId(int userId);

    }
}
