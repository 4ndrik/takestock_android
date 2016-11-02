package com.devabit.takestock.screen.payment.byBACS;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
interface PayByBACSContract {

    interface View extends BaseView<Presenter> {

        void showOfferPaidInView(Offer offer);

        void showNetworkConnectionError();

        void showPaymentError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void makePayment(Payment payment);

    }
}
