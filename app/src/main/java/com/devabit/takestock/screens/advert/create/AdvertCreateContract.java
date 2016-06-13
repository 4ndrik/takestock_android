package com.devabit.takestock.screens.advert.create;

import android.net.Uri;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

import java.io.File;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public interface AdvertCreateContract {

    interface View extends BaseView<Presenter> {

        void showEmptyPhotosError();

        void showEmptyTitleError();

        void showEmptyItemCountError();

        void showEmptyMinimumOrderError();

        void showEmptyGuidePriceError();

        void showEmptyDescriptionError();

        void showEmptyLocationError();

        void showEmptyExpiryDateError();

        void showEmptySizeError();

        void showEmptyCertificationError();

        void showEmptyCertificationExtraError();

        void showEmptyTagsError();

        void showNetworkConnectionError();

        void showUnknownError();

        void showAdvertInPreview(Advert advert);

        void showAdvertSaved(Advert advert);

        void showCategoriesInView(List<Category> categories);

        void showPackagingsInView(List<Packaging> packagings);

        void showShippingsInView(List<Shipping> shippings);

        void showConditionsInView(List<Condition> conditions);

        void showSizesInView(List<Size> sizes);

        void showCertificationsInView(List<Certification> certifications);

        void showPhotoInView(Photo photo);

        void setProgressIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void fetchAdvertRelatedData();

        void processPhotoUriToFile(Uri photoUri, File photoFile);

        void previewAdvert(Advert advert);

        void saveAdvert(Advert advert);
    }
}
