package com.devabit.takestock;

import android.app.Application;
import timber.log.Timber;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class TakeStockApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree() {
            @Override protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });
    }
}


