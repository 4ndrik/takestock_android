package com.devabit.takestock.ui.selling;

import com.devabit.takestock.data.model.*;
import com.devabit.takestock.ui.BasePresenter;
import com.devabit.takestock.ui.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public interface SellingContract {

    interface View extends BaseView<Presenter> {

        void showNetworkConnectionError();

        void showCategoriesInView(List<Category> categories);

        void showShippingsInView(List<Shipping> shippings);

        void showConditionsInView(List<Condition> conditions);

        void showSizesInView(List<Size> sizes);

        void showCertificationsInView(List<Certification> certifications);

    }

    interface Presenter extends BasePresenter {

    }
}
