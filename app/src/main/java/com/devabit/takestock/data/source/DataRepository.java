package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.*;
import rx.Observable;

import java.util.List;

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

    @Override public Observable<AuthToken> obtainAuthToken(UserCredentials credentials) {
        return mDataSource.obtainAuthToken(credentials);
    }

    @Override public Observable<String> getCategories() {
        return null;
    }

    @Override public Observable<String> getAdverts() {
        return null;
    }

    @Override public void saveSizes(List<Size> sizeList) {

    }

    @Override public Observable<List<Size>> getSizes() {
        return null;
    }

    @Override public void saveCertifications(List<Certification> certificationList) {

    }

    @Override public Observable<List<Certification>> getCertifications() {
        return null;
    }

    @Override public void saveShipping(List<Shipping> shippingList) {

    }

    @Override public Observable<List<Shipping>> getShipping() {
        return null;
    }

    @Override public void saveConditions(List<Condition> conditionList) {

    }

    @Override public Observable<List<Condition>> getConditions() {
        return null;
    }

}
