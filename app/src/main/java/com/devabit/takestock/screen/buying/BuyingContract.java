package com.devabit.takestock.screen.buying;

import android.util.SparseArray;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.OfferStatus;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

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
