package com.devabit.takestock.screen.profile.edit;

import android.net.Uri;
import com.devabit.takestock.data.models.BusinessType;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.io.File;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public interface ProfileEditContract {

    interface View extends BaseView<Presenter> {

        void showBusinessTypesInView(List<BusinessType> businessTypes);

        void showPhotoInView(String path);

        void showUserUpdatedInView(User user);

        void showNetworkConnectionError();

        void showPhotoError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void processPhotoUriToFile(Uri photoUri, File uniqueFile);

        void updateUser(User user);
    }
}
