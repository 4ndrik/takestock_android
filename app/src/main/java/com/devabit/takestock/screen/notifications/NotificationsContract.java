package com.devabit.takestock.screen.notifications;

import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
interface NotificationsContract {

    interface View extends BaseView<Presenter> {

        void showNotificationsInView(List<Notification> notifications);

        void setProgressIndicator(boolean isActive);

        void showNotificationRemovedInView(Notification notification);

    }

    interface Presenter extends BasePresenter {

        void loadNotifications();

        void removeNotification(Notification notification);

    }
}
