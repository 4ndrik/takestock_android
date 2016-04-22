package com.devabit.takestock.data.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.DataSource;
import com.devabit.takestock.rest.ApiConnection;
import com.devabit.takestock.rest.RestApi;
import rx.Observable;

/**
 *Implementation for retrieving data from the network.
 *
 * Created by Victor Artemyev on 22/04/2016.
 */
public class RemoteDataSource implements RestApi, DataSource {

    private static RemoteDataSource sInstance;

    public static RemoteDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RemoteDataSource(context);
        }
        return sInstance;
    }

    private Context mContext;

    private RemoteDataSource(Context context) {
        mContext = context;
    }

    @Override public Observable<String> obtainTokenAuth(@NonNull String userName, @NonNull String password) {
        return null;
    }

    @Override public Observable<String> verifyToken(@NonNull String token) {
        return null;
    }

    @Override public Observable<String> getCategories() {
        return Observable.fromCallable(ApiConnection.createGET(composeUrl(GET_CATEGORY)));
    }

    @Override public Observable<String> getAdverts() {
        return Observable.fromCallable(ApiConnection.createGET(composeUrl(GET_ADVERTS)));
    }

    @Override public Observable<String> getSizeTypes() {
        return Observable.fromCallable(ApiConnection.createGET(composeUrl(GET_SIZE_TYPES)));
    }

    @Override public Observable<String> getCertifications() {
        return Observable.fromCallable(ApiConnection.createGET(composeUrl(GET_CERTIFICATIONS)));
    }

    @Override public Observable<String> getShipping() {
        return Observable.fromCallable(ApiConnection.createGET(composeUrl(GET_SHIPPING)));
    }

    @Override public Observable<String> getConditions() {
        return Observable.fromCallable(ApiConnection.createGET(composeUrl(GET_CONDITIONS)));
    }

    /**
     * Checks if the device has any active internet connection.
     *
     * @return true device with internet connection, otherwise false.
     */
    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    @Override public String composeUrl(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(API_BASE_URL);
        for (String param : params) {
            stringBuilder.append("/");
            stringBuilder.append(param);
        }
        return stringBuilder.toString();
    }
}
