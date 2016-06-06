package com.devabit.takestock.screens.advert.detail.dialogs;

import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public interface OfferContract {

    interface View extends BaseView<Presenter> {

        void showValidOffer(Offer offer);

        void showTotalPriceInView(String quantity, double totalPrice);

        void showEmptyQuantityError();

        void showEmptyPriceError();

    }

    interface Presenter extends BasePresenter {

        void calculateOfferTotalPrice(String quantity, String price);

        void validateOffer(Offer offer);

    }
}
