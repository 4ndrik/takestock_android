package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.AccessToken;
import com.devabit.takestock.data.model.UserCredentials;
import rx.Observable;

import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class DataRepository implements DataSource {

    private static DataRepository sInstance;

    public static DataRepository getInstance(DataSource dataSource) {
        if (sInstance == null) {
            sInstance = new DataRepository(dataSource);
        }
        return sInstance;
    }

    /**
     * Used to force {@link #getInstance(DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        sInstance = null;
    }

    private final DataSource mDataSource;

    private DataRepository(@NonNull DataSource dataSource) {
        mDataSource = checkNotNull(dataSource);
    }

    @Override public Observable<AccessToken> obtainAccessToken(UserCredentials credentials) {
        return mDataSource.obtainAccessToken(credentials);
    }

    @Override public Observable<String> getCategories() {
        return null;
    }

    @Override public Observable<String> getAdverts() {
        return null;
    }

    @Override public Observable<String> getSizes() {
        return null;
    }

    @Override public Observable<String> getCertifications() {
        return null;
    }

    @Override public Observable<String> getShipping() {
        return null;
    }

    @Override public Observable<String> getConditions() {
        return null;
    }
}
