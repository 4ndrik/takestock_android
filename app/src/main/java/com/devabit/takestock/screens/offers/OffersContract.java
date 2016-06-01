package com.devabit.takestock.screens.offers;

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
public interface OffersContract {

    interface View extends BaseView<Presenter> {

        void showOfferStatusesInView(SparseArray<OfferStatus> statuses);

//        void showOffersInView(List<Offer> offers);
        void showOffersInView(Map<Offer, Advert> offerAdvertMap);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchOffersByUserId(int userId);

    }
}
