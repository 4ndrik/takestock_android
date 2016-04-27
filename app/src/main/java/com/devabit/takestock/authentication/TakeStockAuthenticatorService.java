package com.devabit.takestock.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Victor Artemyev on 27/04/2016.
 */
public class TakeStockAuthenticatorService extends Service {

    @Nullable @Override public IBinder onBind(Intent intent) {
        TakeStockAuthenticator authenticator = new TakeStockAuthenticator(TakeStockAuthenticatorService.this);
        return authenticator.getIBinder();
    }
}
