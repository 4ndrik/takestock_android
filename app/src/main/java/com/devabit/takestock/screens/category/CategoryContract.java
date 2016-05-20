package com.devabit.takestock.screens.category;

import com.devabit.takestock.data.models.Category;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

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
