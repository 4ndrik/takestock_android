package com.devabit.takestock.screen.notificationPreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Notification;

/**
 * Created by Victor Artemyev on 26/10/2016.
 */

public class NotificationPreviewActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context, Notification notification) {
        Intent starter = new Intent(context, NotificationPreviewActivity.class);
        starter.putExtra(Notification.class.getName(), notification);
        return starter;
    }

    @BindView(R.id.notification_text_view) TextView mNotificationTextView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_preview);
        ButterKnife.bind(this);
        Notification notification = getIntent().getParcelableExtra(Notification.class.getName());
        mNotificationTextView.setText(notification.toString());
    }
}
