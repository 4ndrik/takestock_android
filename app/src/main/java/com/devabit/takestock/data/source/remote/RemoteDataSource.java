package com.devabit.takestock.data.source.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import com.devabit.takestock.R;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.remote.filterBuilders.AdvertFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.mappers.*;
import com.devabit.takestock.exceptions.HttpResponseException;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rest.RestApi;
import okhttp3.*;
import org.json.JSONException;
import rx.Observable;
import rx.functions.Action1;
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
                } else {
                    response = chain.proceed(request);
                }

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
                String tokeJson = new AuthTokenJsonMapper().toJsonString(authToken);
                Request request = buildPOSTRequest(composeUrl(POST_TOKEN_VERIFY), tokeJson);
                try(ResponseBody body = mOkHttpClient.newCall(request).execute().body()) {
                    return body.string();
                }
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
                throw new HttpResponseException(code, response.message(), response.body().string());
            default:
                return response;
        }
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignUp(UserCredentials credentials) {
        return buildUserCredentialsAsJsonStringObservable(credentials)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOST(composeUrl(POST_TOKEN_REGISTER), json));
                    }
                }).map(new Func1<String, AuthToken>() {
                    @Override public AuthToken call(String json) {
                        try {
                            return new AuthTokenJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignIn(UserCredentials credentials) {
        return buildUserCredentialsAsJsonStringObservable(credentials)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOST(composeUrl(POST_TOKEN_AUTH), json));
                    }
                }).map(new Func1<String, AuthToken>() {
                    @Override public AuthToken call(String json) {
                        try {
                            return new AuthTokenJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private Observable<String> buildUserCredentialsAsJsonStringObservable(UserCredentials credentials) {
        return Observable.just(credentials)
                .map(new Func1<UserCredentials, String>() {
                    @Override public String call(UserCredentials userCredentials) {
                        try {
                            return new UserCredentialsJsonMapper().toJsonString(userCredentials);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public void saveCategories(List<Category> categories) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> getCategories() {
        return Observable.fromCallable(createGET(composeUrl(GET_CATEGORY)))
                .map(new Func1<String, List<Category>>() {
                    @Override public List<Category> call(String json) {
                        try {
                            return new CategoryAndSubcategoryJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<List<Category>>() {
                    @Override public void call(List<Category> categories) {
                        LOGD(TAG, "Categories from RemoteDataSource " + categories);
                    }
                });
    }

    @Override public Observable<Advert> saveOrUpdateAdvert(final Advert advert) {
        return Observable.just(advert)
                .map(new Func1<Advert, String>() {
                    @Override public String call(Advert advert) {
                        try {
                            return new AdvertJsonMapper().toJsonString(advert);
                        } catch (JSONException e) {
                            throw new RuntimeException();
                        }
                    }
                }).flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOST(composeUrl(GET_ADVERTS), json));
                    }
                }).map(new Func1<String, Advert>() {
                    @Override public Advert call(String jsonString) {
                        try {
                            return new AdvertJsonMapper().fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<ResultList<Advert>> getResultAdvertList() {
        return getResultAdvertListPerPage(composeUrl(GET_ADVERTS));
    }

    @Override public Observable<ResultList<Advert>> getResultAdvertListPerPage(String page) {
        return Observable.fromCallable(createGET(page))
                .map(new Func1<String, ResultList<Advert>>() {
                    @Override public ResultList<Advert> call(String json) {
                        try {
                            LOGD(TAG, json);
                            return new AdvertResultListJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).doOnNext(new Action1<ResultList<Advert>>() {
                    @Override public void call(ResultList<Advert> advertResultList) {
                        LOGD(TAG, "ResultAdvertList: " + advertResultList);
                    }
                });
    }

    @Override public Observable<ResultList<Advert>> getResultAdvertListPerFilter(final AdvertFilter filter) {
        return Observable.just(filter)
                .map(new Func1<AdvertFilter, String>() {
                    @Override public String call(AdvertFilter filter) {
                        return new AdvertFilterUrlBuilder().buildUrl(filter);
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override public void call(String s) {
                        LOGD(TAG, s);
                    }
                })
                .flatMap(new Func1<String, Observable<ResultList<Advert>>>() {
                    @Override public Observable<ResultList<Advert>> call(String url) {
                        return getResultAdvertListPerPage(url);
                    }
                });
    }

    @Override public void saveSizes(List<Size> sizeList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        return Observable.fromCallable(createGET(composeUrl(GET_SIZE_TYPES)))
                .map(new Func1<String, List<Size>>() {
                    @Override public List<Size> call(String json) {
                        try {
                            return new SizeJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).doOnNext(new Action1<List<Size>>() {
                    @Override public void call(List<Size> sizes) {
                        LOGD(TAG, "Sizes from RemoteDataSource " + sizes);
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
                            return new CertificationJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<List<Certification>>() {
                    @Override public void call(List<Certification> certifications) {
                        LOGD(TAG, "Certifications from RemoteDataSource " + certifications);
                    }
                });
    }

    @Override public Certification getCertificationById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public void saveShippings(List<Shipping> shippingList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShippings() {
        return Observable.fromCallable(createGET(composeUrl(GET_SHIPPING)))
                .map(new Func1<String, List<Shipping>>() {
                    @Override public List<Shipping> call(String json) {
                        try {
                            return new ShippingJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).doOnNext(new Action1<List<Shipping>>() {
                    @Override public void call(List<Shipping> shippings) {
                        LOGD(TAG, "Shippings from RemoteDataSource " + shippings);
                    }
                });
    }

    @Override public Shipping getShippingById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public void saveConditions(List<Condition> conditionList) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        return Observable.fromCallable(createGET(composeUrl(GET_CONDITIONS)))
                .map(new Func1<String, List<Condition>>() {
                    @Override public List<Condition> call(String json) {
                        try {
                            return new ConditionJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException();
                        }
                    }
                }).doOnNext(new Action1<List<Condition>>() {
                    @Override public void call(List<Condition> conditions) {
                        LOGD(TAG, "Conditions from RemoteDataSource " + conditions);
                    }
                });
    }

    @Override public Condition getConditionById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public void savePackagings(List<Packaging> packagings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Packaging>> getPackagings() {
        return Observable.fromCallable(createGET(composeUrl(GET_PACKAGING)))
                .map(new Func1<String, List<Packaging>>() {
                    @Override public List<Packaging> call(String json) {
                        try {
                            return new PackagingJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException();
                        }
                    }
                }).doOnNext(new Action1<List<Packaging>>() {
                    @Override public void call(List<Packaging> packagings) {
                        LOGD(TAG, "Packaging from RemoteDataSource " + packagings);
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
                    try(ResponseBody body = response.body()) {
                        return body.string();
                    }
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
                    try(ResponseBody body = response.body()) {
                        return body.string();
                    }
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
        stringBuilder.append(SCHEME);
        stringBuilder.append("://");
        stringBuilder.append(API_BASE_URL);
        for (String param : params) {
            stringBuilder.append("/");
            stringBuilder.append(param);
        }
        return stringBuilder.toString();
    }
}
