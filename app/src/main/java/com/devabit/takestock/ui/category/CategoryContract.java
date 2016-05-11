package com.devabit.takestock.ui.category;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.ui.BasePresenter;
import com.devabit.takestock.ui.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public interface CategoryContract {

    interface View extends BaseView<Presenter> {

        void showCategoriesInView(List<Category> categories);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {
        void fetchCategories();
    }
}
