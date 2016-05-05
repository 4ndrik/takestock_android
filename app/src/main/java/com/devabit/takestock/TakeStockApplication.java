package com.devabit.takestock;

import android.app.Application;
import android.graphics.Bitmap;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Victor Artemyev on 07/04/2016.
 */
public class TakeStockApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Picasso.Builder builder = new Picasso.Builder(TakeStockApplication.this);
        builder.downloader(new OkHttp3Downloader(TakeStockApplication.this, Integer.MAX_VALUE));
        builder.defaultBitmapConfig(Bitmap.Config.RGB_565);
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(BuildConfig.LOGGING_ENABLED);
        built.setLoggingEnabled(BuildConfig.LOGGING_ENABLED);
        Picasso.setSingletonInstance(built);
    }
}


