package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.model.Advert;
import rx.Observable;
import rx.functions.Action1;

import java.util.List;

import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class DataRepository implements DataSource {

    private static DataRepository sInstance;

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @param localDataSource  the device storage data source
     * @return the {@link DataRepository} instance
     */
    public static DataRepository getInstance(DataSource remoteDataSource,
                                             DataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new DataRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;

    private DataRepository(@NonNull DataSource remoteDataSource,
                           @NonNull DataSource localDataSource) {
        mRemoteDataSource = checkNotNull(remoteDataSource);
        mLocalDataSource = checkNotNull(localDataSource);
    }

    /**
     * Used to force {@link #getInstance(DataSource, DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        sInstance = null;
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignUp(UserCredentials credentials) {
        return mRemoteDataSource.obtainAuthTokenPerSignUp(credentials);
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignIn(UserCredentials credentials) {
        return mRemoteDataSource.obtainAuthTokenPerSignIn(credentials);
    }

    @Override public void saveCategories(List<Category> categories) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> getCategories() {
        Observable<List<Category>> localCategories = mLocalDataSource.getCategories();
        Observable<List<Category>> remoteCategories = mRemoteDataSource.getCategories()
                .doOnNext(new Action1<List<Category>>() {
                    @Override public void call(List<Category> categories) {
                        mLocalDataSource.saveCategories(categories);
                    }
                });
        return Observable.concat(localCategories, remoteCategories).first();
    }

    @Override public Observable<List<Advert>> getAdverts() {
        return mRemoteDataSource.getAdverts();
    }

    @Override public void saveSizes(List<Size> sizes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        Observable<List<Size>> localSizes = mLocalDataSource.getSizes();
        Observable<List<Size>> remoteSizes = mRemoteDataSource.getSizes()
                .doOnNext(new Action1<List<Size>>() {
                    @Override public void call(List<Size> sizes) {
                        mLocalDataSource.saveSizes(sizes);
                    }
                });
        return Observable.concat(localSizes, remoteSizes).first();
    }

    @Override public void saveCertifications(List<Certification> certifications) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> getCertifications() {
        Observable<List<Certification>> localCertifications = mLocalDataSource.getCertifications();
        Observable<List<Certification>> remoteCertifications = mRemoteDataSource.getCertifications()
                .doOnNext(new Action1<List<Certification>>() {
                    @Override public void call(List<Certification> certifications) {
                        mLocalDataSource.saveCertifications(certifications);
                    }
                });
        return Observable.concat(localCertifications, remoteCertifications).first();
    }

    @Override public void saveShippings(List<Shipping> shippings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShippings() {
        Observable<List<Shipping>> localShippings = mLocalDataSource.getShippings();
        Observable<List<Shipping>> remoteShippings = mRemoteDataSource.getShippings()
                .doOnNext(new Action1<List<Shipping>>() {
                    @Override public void call(List<Shipping> shippings) {
                        mLocalDataSource.saveShippings(shippings);
                    }
                });
        return Observable.concat(localShippings, remoteShippings).first();
    }

    @Override public void saveConditions(List<Condition> conditions) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        Observable<List<Condition>> localConditions = mLocalDataSource.getConditions();
        Observable<List<Condition>> remoteConditions = mRemoteDataSource.getConditions()
                .doOnNext(new Action1<List<Condition>>() {
                    @Override public void call(List<Condition> conditions) {
                        mLocalDataSource.saveConditions(conditions);
                    }
                });
        return Observable.concat(localConditions, remoteConditions).first();
    }

}
