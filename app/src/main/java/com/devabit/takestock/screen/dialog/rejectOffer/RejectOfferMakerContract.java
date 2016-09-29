package com.devabit.takestock.screen.dialog.rejectOffer;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public interface RejectOfferMakerContract {

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
