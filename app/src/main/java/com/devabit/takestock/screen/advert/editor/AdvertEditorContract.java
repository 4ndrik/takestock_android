package com.devabit.takestock.screen.advert.editor;

import com.devabit.takestock.data.model.*;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
interface AdvertEditorContract {

    interface View extends BaseView<Presenter> {

        void showCategoriesInView(List<Category> categories);

        void showPackagingsInView(List<Packaging> packagings);

        void showShippingsInView(List<Shipping> shippings);

        void showConditionsInView(List<Condition> conditions);

        void showSizesInView(List<Size> sizes);

        void showCertificationsInView(List<Certification> certifications);

        void showAdvertRelatedDataFetched();

        void setProgressIndicator(boolean isActive);

        void showEmptyPhotosError();

        void showEmptyTitleError();

        void showEmptyCategoryError();

        void showEmptySubcategoryError();

        void showEmptyItemCountError();

        void showEmptyMinimumOrderError();

        void showEmptyGuidePriceError();

        void showEmptyDescriptionError();

        void showEmptyLocationError();

        void showEmptyShippingError();

        void showEmptyConditionError();

        void showEmptyExpiryDateError();

        void showEmptySizeError();

        void showEmptyCertificationError();

        void showEmptyCertificationExtraError();

        void showEmptyTagsError();

        void showNetworkConnectionError();

        void showUnknownError();

        void showSavedAdvertInView(Advert advert);

        void showEditedAdvertInView(Advert advert);

        void showPreviewedAdvertInView(Advert advert);

    }

    interface Presenter extends BasePresenter {

        void fetchAdvertRelatedData();

        void saveAdvert(Advert advert);

        void editAdvert(Advert advert);

        void previewAdvert(Advert advert);
    }
}
