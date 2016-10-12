package com.devabit.takestock.screen.advert.active;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

/**
 * Created by Victor Artemyev on 30/09/2016.
 */

interface AdvertActiveContract {

    interface View extends BaseView<Presenter> {

        void showUnnotifiedAdvertInView(Advert advert);
    }

    interface Presenter extends BasePresenter {
        void unnotifyAdvert();
    }

}
