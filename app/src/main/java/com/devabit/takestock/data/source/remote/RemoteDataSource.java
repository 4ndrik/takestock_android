package com.devabit.takestock.data.source.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.remote.mapper.*;
import com.devabit.takestock.exceptions.HttpResponseException;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rest.RestApi;
import okhttp3.*;
import org.json.JSONException;
import rx.Observable;
import rx.functions.Func1;

import java.io.IOException;
import java.util.List;
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

    private static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_VALUE_JSON = "application/json; charset=utf-8";
    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";
    private static final String HEADER_VALUE_TOKEN = "JWT ";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse(HEADER_VALUE_JSON);

    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;

    private Context mContext;
    private final ConnectivityManager mConnectivityManager;

    private final AccountManager mAccountManager;
    private final String mAccountType;
    private final String mTokenType;

    private final OkHttpClient mOkHttpClient;

    private RemoteDataSource(Context context) {
        mContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mAccountManager = AccountManager.get(mContext);
        mAccountType = mContext.getString(R.string.authenticator_account_type);
        mTokenType = mContext.getString(R.string.authenticator_token_type);
        mOkHttpClient = buildClient();
    }

    private OkHttpClient buildClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(getAuthInterceptor());
        return builder.build();
    }

    private Interceptor getAuthInterceptor() {
        return new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                builder.header(HEADER_NAME_CONTENT_TYPE, HEADER_VALUE_JSON);

                Account account = getAccountOrNull();
                Response response;
                if (account != null) {
                    String authToken = getAuthToken(account);
                    setAuthHeader(builder, authToken);
                    request = builder.build();
                    response = chain.proceed(request);

                    if (response.code() == 401) { //if unauthorized
                        authToken = refreshAuthToken(authToken);
                        setAuthToken(account, authToken);
                        setAuthHeader(builder, authToken);
                    }
                }
                response = chain.proceed(request);
                return checkResponsePerCode(response);
            }
        };
    }

    @Nullable private Account getAccountOrNull() {
        Account[] accounts = mAccountManager.getAccountsByType(mAccountType);
        if (accounts.length > 0) {
            return accounts[0];
        } else {
            return null;
        }
    }

    private String getAuthToken(Account account) {
        return mAccountManager.peekAuthToken(account, mTokenType);
    }

    private String refreshAuthToken(String token) {
        synchronized (mOkHttpClient) {
            AuthToken authToken = new AuthToken();
            authToken.token = token;
            try {
                String tokeJson = new AuthTokenMapper().toJsonString(authToken);
                Request request = buildPOSTRequest(composeUrl(POST_TOKEN_VERIFY), tokeJson);
                return mOkHttpClient.newCall(request).execute().body().string();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setAuthToken(Account account, String authToken) {
        mAccountManager.setAuthToken(account, mTokenType, authToken);
    }

    private void setAuthHeader(Request.Builder builder, String authToken) {
        if (authToken == null) return;
        builder.header(HEADER_NAME_AUTHORIZATION, HEADER_VALUE_TOKEN + authToken);
    }

    private Response checkResponsePerCode(Response response) throws IOException {
        int code = response.code();
        switch (code) {
            case 400: //if bad request
                throw new HttpResponseException(code, response.message());
            default:
                return response;
        }
    }

    @Override public Observable<AuthToken> obtainAuthToken(UserCredentials credentials) {
        return Observable.just(credentials)
                .map(new Func1<UserCredentials, String>() {
                    @Override public String call(UserCredentials userCredentials) {
                        try {
                            return new UserCredentialsMapper().toJsonString(userCredentials);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOST(composeUrl(POST_TOKEN_AUTH), json));
                    }
                }).map(new Func1<String, AuthToken>() {
                    @Override public AuthToken call(String json) {
                        try {
                            return new AuthTokenMapper().fromJsonString(json);
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

    @Override public void saveSizes(List<Size> sizeList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        return Observable.fromCallable(createGET(composeUrl(GET_SIZE_TYPES)))
                .map(new Func1<String, List<Size>>() {
                    @Override public List<Size> call(String json) {
                        try {
                            return new SizeMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public void saveCertifications(List<Certification> certificationList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> getCertifications() {
        return Observable.fromCallable(createGET(composeUrl(GET_CERTIFICATIONS)))
                .map(new Func1<String, List<Certification>>() {
                    @Override public List<Certification> call(String json) {
                        try {
                            return new CertificationMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public void saveShipping(List<Shipping> shippingList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShipping() {
        return Observable.fromCallable(createGET(composeUrl(GET_SHIPPING)))
                .map(new Func1<String, List<Shipping>>() {
                    @Override public List<Shipping> call(String json) {
                        try {
                            return new ShippingMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public void saveConditions(List<Condition> conditionList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        return Observable.fromCallable(createGET(composeUrl(GET_CONDITIONS)))
                .map(new Func1<String, List<Condition>>() {
                    @Override public List<Condition> call(String json) {
                        try {
                            return new ConditionMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException();
                        }
                    }
                });
    }

    private Callable<String> createPOST(final String url, final String json) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                if (isThereInternetConnection()) {
                    Request request = buildPOSTRequest(url, json);
                    LOGD(TAG, request.toString());
                    Response response = mOkHttpClient.newCall(request).execute();
                    return response.body().string();
                } else {
                    throw new NetworkConnectionException("There is no internet connection.");
                }
            }
        };
    }

    private Request buildPOSTRequest(String url, String json) throws Exception {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
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

        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
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