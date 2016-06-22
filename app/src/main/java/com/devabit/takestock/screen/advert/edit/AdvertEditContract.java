package com.devabit.takestock.screen.advert.edit;

import android.net.Uri;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.io.File;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public interface AdvertEditContract {

    interface View extends BaseView<Presenter> {

        void showCategoriesInView(List<Category> categories);

        void showPackagingsInView(List<Packaging> packagings);

        void showShippingsInView(List<Shipping> shippings);

        void showConditionsInView(List<Condition> conditions);

        void showSizesInView(List<Size> sizes);

        void showCertificationsInView(List<Certification> certifications);

        void onAdvertRelatedDataShowed();

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

        void setProgressIndicator(boolean isActive);

        //        void showAdvertInPreview(Advert advert);
        void showPhotoInView(Photo photo);

        void showAdvertUpdated(Advert advert);

    }

    interface Presenter extends BasePresenter {

        void fetchAdvertRelatedData();

        void processPhotoUriToFile(Uri photoUri, File photoFile);

        void previewAdvert(Advert advert);

        void updateAdvert(Advert advert);
    }
}
