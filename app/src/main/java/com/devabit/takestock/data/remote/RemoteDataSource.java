package com.devabit.takestock.data.remote;

import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.devabit.takestock.data.DataSource;
import com.devabit.takestock.data.model.AccessToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.exceptions.HttpResponseException;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rest.RestApi;
import okhttp3.*;
import org.json.JSONException;
import rx.Observable;
import rx.functions.Func1;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Implementation for retrieving data from the network.
 * <p/>
 * Created by Victor Artemyev on 22/04/2016.
 */
public class RemoteDataSource implements RestApi, DataSource {

    private static final String TAG = makeLogTag(RemoteDataSource.class);

    private static RemoteDataSource sInstance;

    public static RemoteDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RemoteDataSource(context);
        }
        return sInstance;
    }

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse(CONTENT_TYPE_VALUE_JSON);

    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;

    private Context mContext;
    private AccountManager mAccountManager;
    private OkHttpClient mOkHttpClient;

    private RemoteDataSource(Context context) {
        mContext = context.getApplicationContext();
        mAccountManager = AccountManager.get(mContext);
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
                Request.Builder builder = request.newBuilder();
                builder.addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
                Response response = chain.proceed(request);
                switch (response.code()) {
                    case 400:
                        throw new HttpResponseException(response.message());

                    case 401:
                        //TODO: implement refresh token

                    default:
                        return response;
                }
            }
        };
    }

    @Override public Observable<AccessToken> obtainAccessToken(UserCredentials credentials) {
        return Observable.fromCallable(createPOST(composeUrl(POST_TOKEN_AUTH), credentials))
                .map(new Func1<String, AccessToken>() {
                    @Override public AccessToken call(String jsonString) {
                        try {
                            LOGD(TAG, jsonString);
                            return AccessToken.fromJson(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<String> getCategories() {
        return Observable.fromCallable(createGET(composeUrl(GET_CATEGORY)));
    }

    @Override public Observable<String> getAdverts() {
        return Observable.fromCallable(createGET(composeUrl(GET_ADVERTS)));
    }

    @Override public Observable<String> getSizeTypes() {
        return Observable.fromCallable(createGET(composeUrl(GET_SIZE_TYPES)));
    }

    @Override public Observable<String> getCertifications() {
        return Observable.fromCallable(createGET(composeUrl(GET_CERTIFICATIONS)));
    }

    @Override public Observable<String> getShipping() {
        return Observable.fromCallable(createGET(composeUrl(GET_SHIPPING)));
    }

    @Override public Observable<String> getConditions() {
        return Observable.fromCallable(createGET(composeUrl(GET_CONDITIONS)));
    }

    private Callable<String> createPOST(final String url, final RequestObject requestObject) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                if (isThereInternetConnection()) {
                    Request request = buildPOSTRequest(url, requestObject);
                    LOGD(TAG, request.toString());
                    Response response = mOkHttpClient.newCall(request).execute();
                    return response.body().string();
                } else {
                    throw new NetworkConnectionException("There is no internet connection.");
                }
            }
        };
    }

    private Request buildPOSTRequest(String url, RequestObject requestObject) throws Exception {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, requestObject.toJsonString());
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    private Callable<String> createGET(final String url) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                if (isThereInternetConnection()) {
                    Request request = buildGETRequest(url);
                    LOGD(TAG, request.toString());
                    Response response = mOkHttpClient.newCall(request).execute();
                    return response.body().string();
                } else {
                    throw new NetworkConnectionException("There is no internet connection.");
                }
            }
        };
    }

    private Request buildGETRequest(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
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
