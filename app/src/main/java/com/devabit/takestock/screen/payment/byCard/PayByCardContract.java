package com.devabit.takestock.screen.payment.byCard;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;
import com.stripe.android.model.Card;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
interface PayByCardContract {

    interface View extends BaseView<Presenter> {

        void showPaymentRateInView(int rate);

        void showOfferPaidInView(Offer offer);

        void showCardAmericanExpressInView();

        void showCardDiscoverInView();

        void showCardJCBInView();

        void showCardDinnersClubInView();

        void showCardVisaInView();

        void showCardMastercardInView();

        void showCardUnknownInView();

        void showCardNumberError();

        void showExpiryDateError();

        void showCVVCodeError();

        void showNetworkConnectionError();

        void showUnknownError();

        void showPaymentError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void loadPaymentRate();

        void validateCardNumber(String number);

        void makePayment(Offer offer, Card card);

    }
}
