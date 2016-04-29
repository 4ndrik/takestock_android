package com.devabit.takestock.data.source.local;

import android.content.Context;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Observable;

import java.util.List;

import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 28/04/2016.
 */
public class LocalDataSource implements DataSource {

    private static final String TAG = makeLogTag(LocalDataSource.class);

    private static LocalDataSource sInstance;

    public static LocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocalDataSource(context);
        }
        return sInstance;
    }

    private static final String DATA_BASE_NAME = "takestock.realm";

    private final Realm mRealm;

    private LocalDataSource(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(DATA_BASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(config);
    }

    @Override public Observable<AuthToken> obtainAuthToken(UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
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
