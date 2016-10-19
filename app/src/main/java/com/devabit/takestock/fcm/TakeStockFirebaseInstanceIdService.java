package com.devabit.takestock.fcm;

import com.devabit.takestock.TakeStockPref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import timber.log.Timber;

/**
 * Created by Victor Artemyev on 17/10/2016.
 */

public class TakeStockFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: %s", refreshedToken);
        TakeStockPref.putFCMToken(TakeStockFirebaseInstanceIdService.this, refreshedToken);
    }
}
