package com.devabit.takestock.screen.payment;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;
import com.stripe.android.model.Card;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
interface PaymentContract {

    interface View extends BaseView<Presenter> {

        void showPaymentMadeInView(Payment payment);

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

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void validateCardNumber(String number);

        void makePayment(Offer offer, Card card);

    }
}
