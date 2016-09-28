package com.devabit.takestock.screen.offer;

import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

interface OfferContract {

    interface View extends BaseView<Presenter> {

        void showNetworkConnectionError();

        void showUnknownError();

    }

    interface Presenter extends BasePresenter {

    }
}
