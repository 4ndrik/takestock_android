package com.devabit.takestock.screen.payment.byBACS;

import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
interface PayByBACSContract {

    interface View extends BaseView<Presenter> {

        void showPaymentMadeInView(Payment payment);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void makePayment(Payment payment);

    }
}
