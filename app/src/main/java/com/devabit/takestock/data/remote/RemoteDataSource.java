package com.devabit.takestock.data.remote;

import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.DataSource;
import com.devabit.takestock.rest.ApiConnection;
import com.devabit.takestock.rest.RestApi;
import okhttp3.*;
import rx.Observable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for retrieving data from the network.
 * <p/>
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

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;

    private Context mContext;
    private AccountManager mAccountManager;
    private OkHttpClient mOkHttpClient;

    private RemoteDataSource(Context context) {
        mContext = context;
        mAccountManager = AccountManager.get(context);
        mOkHttpClient = buildClient(mAccountManager);
    }

    private OkHttpClient buildClient(AccountManager accountManager) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(getAuthInterceptor(accountManager));

        return builder.build();
    }

    private Interceptor getAuthInterceptor(final AccountManager accountManager) {
        return new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                return null;
            }
        };
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
