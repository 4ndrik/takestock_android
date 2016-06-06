package com.devabit.takestock.data.source.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.devabit.takestock.R;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.filters.OfferFilter;
import com.devabit.takestock.data.filters.QuestionFilter;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.remote.filterBuilders.AdvertFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.filterBuilders.OfferFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.filterBuilders.QuestionFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.mappers.*;
import com.devabit.takestock.exceptions.HttpResponseException;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rest.RestApi;
import okhttp3.*;
import org.json.JSONException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import java.io.IOException;
import java.util.ArrayList;
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
                Request request = buildPOSTRequest(TOKEN_VERIFY, tokeJson);
                try (ResponseBody body = mOkHttpClient.newCall(request).execute().body()) {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //     Method for data source
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override public Observable<AuthToken> obtainAuthTokenPerSignUp(@NonNull UserCredentials credentials) {
        return buildUserCredentialsAsJsonStringObservable(credentials)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(TOKEN_REGISTER, json));
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

    @Override public Observable<AuthToken> obtainAuthTokenPerSignIn(@NonNull UserCredentials credentials) {
        return buildUserCredentialsAsJsonStringObservable(credentials)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(TOKEN_AUTH, json));
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

    @Override public void saveCategories(@NonNull List<Category> categories) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> updateCategories() {
        return getCategories();
    }

    @Override public Observable<List<Category>> getCategories() {
        return Observable.fromCallable(createGETCallable(CATEGORY))
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

    @Override public Category getCategoryById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public void saveSizes(@NonNull List<Size> sizes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> updateSizes() {
        return getSizes();
    }

    @Override public Observable<List<Size>> getSizes() {
        return Observable.fromCallable(createGETCallable(SIZE_TYPES))
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

    @Override public void saveCertifications(@NonNull List<Certification> certifications) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> updateCertifications() {
        return getCertifications();
    }

    @Override public Observable<List<Certification>> getCertifications() {
        return Observable.fromCallable(createGETCallable(CERTIFICATIONS))
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

    @Override public void saveShippings(@NonNull List<Shipping> shippings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> updateShippings() {
        return getShippings();
    }

    @Override public Observable<List<Shipping>> getShippings() {
        return Observable.fromCallable(createGETCallable(SHIPPING))
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

    @Override public void saveConditions(@NonNull List<Condition> conditions) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> updateConditions() {
        return getConditions();
    }

    @Override public Observable<List<Condition>> getConditions() {
        return Observable.fromCallable(createGETCallable(CONDITIONS))
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

    @Override public void savePackagings(@NonNull List<Packaging> packagings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Packaging>> updatePackagings() {
        return getPackagings();
    }

    @Override public Observable<List<Packaging>> getPackagings() {
        return Observable.fromCallable(createGETCallable(PACKAGING))
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

    @Override public Packaging getPackagingById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public void saveOfferStatuses(@NonNull List<OfferStatus> statuses) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<OfferStatus>> updateOfferStatuses() {
        return getOfferStatuses();
    }

    @Override public Observable<List<OfferStatus>> getOfferStatuses() {
        return Observable.fromCallable(createGETCallable(OFFER_STATUS))
                .map(new Func1<String, List<OfferStatus>>() {
                    @Override public List<OfferStatus> call(String json) {
                        try {
                            return new OfferStatusJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).doOnNext(new Action1<List<OfferStatus>>() {
                    @Override public void call(List<OfferStatus> statuses) {
                        LOGD(TAG, "Offer Statuses from RemoteDataSource " + statuses);
                    }
                });
    }

    @Override public OfferStatus getOfferStatusById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert> saveAdvert(@NonNull Advert advert) {
        final AdvertJsonMapper jsonMapper = new AdvertJsonMapper();
        return Observable.just(advert)
                .map(new Func1<Advert, String>() {
                    @Override public String call(Advert advert) {
                        try {
                            return jsonMapper.toJsonString(advert);
                        } catch (JSONException e) {
                            throw new RuntimeException();
                        }
                    }
                }).flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(ADVERTS, json));
                    }
                }).map(new Func1<String, Advert>() {
                    @Override public Advert call(String jsonString) {
                        try {
                            return jsonMapper.fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<List<Advert>> getAdvertsPerFilter(@NonNull AdvertFilter filter) {
        return Observable.just(filter)
                .map(new Func1<AdvertFilter, String>() {
                    @Override public String call(AdvertFilter advertFilter) {
                        return new AdvertFilterUrlBuilder(ADVERTS, advertFilter).buildUrl();
                    }
                })
                .flatMap(new Func1<String, Observable<List<Advert>>>() {
                    @Override public Observable<List<Advert>> call(final String page) {
                        return Observable.create(new Observable.OnSubscribe<List<Advert>>() {
                            @Override public void call(Subscriber<? super List<Advert>> subscriber) {
                                try {
                                    AdvertResultListJsonMapper jsonMapper = new AdvertResultListJsonMapper();
                                    ResultList<Advert> resultList = jsonMapper.fromJsonString(createGET(page));
                                    List<Advert> result = new ArrayList<>(resultList.getResults());
                                    while (resultList.hasNext()) {
                                        String nextPage = resultList.getNext();
                                        resultList = jsonMapper.fromJsonString(createGET(nextPage));
                                        result.addAll(resultList.getResults());
                                    }
                                    subscriber.onNext(result);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                });
    }

    @Override public Observable<ResultList<Advert>> getAdvertResultListPerFilter(@NonNull AdvertFilter filter) {
        return Observable.just(filter)
                .map(new Func1<AdvertFilter, String>() {
                    @Override public String call(AdvertFilter filter) {
                        return new AdvertFilterUrlBuilder(ADVERTS, filter).buildUrl();
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override public void call(String url) {
                        LOGD(TAG, url);
                    }
                })
                .flatMap(new Func1<String, Observable<ResultList<Advert>>>() {
                    @Override public Observable<ResultList<Advert>> call(String url) {
                        return getAdvertResultListPerPage(url);
                    }
                });
    }

    @Override public Observable<ResultList<Advert>> getAdvertResultListPerPage(@NonNull String page) {
        return Observable.fromCallable(createGETCallable(page))
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

    @Override public Observable<Offer> saveOffer(@NonNull Offer offer) {
        final OfferJsonMapper jsonMapper = new OfferJsonMapper();
        return Observable.just(offer)
                .map(new Func1<Offer, String>() {
                    @Override public String call(Offer offer) {
                        try {
                            return jsonMapper.toJsonString(offer);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override public void call(String json) {
                        LOGD(TAG, "Offer json: " + json);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String jsonString) {
                        return Observable.fromCallable(createPOSTCallable(OFFERS, jsonString));
                    }
                })
                .map(new Func1<String, Offer>() {
                    @Override public Offer call(String jsonString) {
                        try {
                            LOGD(TAG, "Offer saved json: " + jsonString);
                            return jsonMapper.fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<Offer> updateOffer(@NonNull final Offer offer) {
        final OfferJsonMapper jsonMapper = new OfferJsonMapper();
        return Observable.just(offer)
                .map(new Func1<Offer, String>() {
                    @Override public String call(Offer offer) {
                        try {
                            return jsonMapper.toJsonString(offer);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override public void call(String json) {
                        LOGD(TAG, "Offer json: " + json);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String jsonString) {
                        return Observable.fromCallable(createPUTCallable(OFFERS + offer.getId() + "/", jsonString));
                    }
                })
                .map(new Func1<String, Offer>() {
                    @Override public Offer call(String jsonString) {
                        try {
                            LOGD(TAG, "Offer updated json: " + jsonString);
                            return jsonMapper.fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<List<Offer>> getOffersPerFilter(@NonNull OfferFilter filter) {
        return Observable.just(filter)
                .map(new Func1<OfferFilter, String>() {
                    @Override public String call(OfferFilter advertFilter) {
                        return new OfferFilterUrlBuilder(OFFERS, advertFilter).buildUrl();
                    }
                })
                .flatMap(new Func1<String, Observable<List<Offer>>>() {
                    @Override public Observable<List<Offer>> call(final String page) {
                        return Observable.create(new Observable.OnSubscribe<List<Offer>>() {
                            @Override public void call(Subscriber<? super List<Offer>> subscriber) {
                                try {
                                    OfferResultListJsonMapper jsonMapper = new OfferResultListJsonMapper();
                                    ResultList<Offer> resultList = jsonMapper.fromJsonString(createGET(page));
                                    List<Offer> result = new ArrayList<>(resultList.getResults());
                                    while (resultList.hasNext()) {
                                        String nextPage = resultList.getNext();
                                        resultList = jsonMapper.fromJsonString(createGET(nextPage));
                                        result.addAll(resultList.getResults());
                                    }
                                    subscriber.onNext(result);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                });
    }

    @Override public Observable<ResultList<Offer>> getOfferResultListPerFilter(@NonNull OfferFilter filter) {
        return Observable.just(filter)
                .map(new Func1<OfferFilter, String>() {
                    @Override public String call(OfferFilter offerFilter) {
                        return new OfferFilterUrlBuilder(OFFERS, offerFilter).buildUrl();
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override public void call(String url) {
                        LOGD(TAG, url);
                    }
                })
                .flatMap(new Func1<String, Observable<ResultList<Offer>>>() {
                    @Override public Observable<ResultList<Offer>> call(String url) {
                        return getOfferResultListPerPage(url);
                    }
                });
    }

    @Override public Observable<ResultList<Offer>> getOfferResultListPerPage(@NonNull String page) {
        return Observable.fromCallable(createGETCallable(page))
                .map(new Func1<String, ResultList<Offer>>() {
                    @Override public ResultList<Offer> call(String jsonString) {
                        try {
                            return new OfferResultListJsonMapper().fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<Question> saveQuestion(Question question) {
        final QuestionJsonMapper jsonMapper = new QuestionJsonMapper();
        return Observable.just(question)
                .map(new Func1<Question, String>() {
                    @Override public String call(Question question) {
                        try {
                            return jsonMapper.toJsonString(question);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(QUESTIONS, json));
                    }
                })
                .map(new Func1<String, Question>() {
                    @Override public Question call(String json) {
                        try {
                            return jsonMapper.fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<ResultList<Question>> getQuestionResultListPerFilter(@NonNull QuestionFilter filter) {
        return Observable.just(filter)
                .map(new Func1<QuestionFilter, String>() {
                    @Override public String call(QuestionFilter filter) {
                        return new QuestionFilterUrlBuilder(QUESTIONS, filter).buildUrl();
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override public void call(String url) {
                        LOGD(TAG, url);
                    }
                })
                .flatMap(new Func1<String, Observable<ResultList<Question>>>() {
                    @Override public Observable<ResultList<Question>> call(String page) {
                        return getQuestionResultListPerPage(page);
                    }
                });
    }

    @Override public Observable<ResultList<Question>> getQuestionResultListPerPage(@NonNull String page) {
        return Observable.fromCallable(createGETCallable(page))
                .map(new Func1<String, ResultList<Question>>() {
                    @Override public ResultList<Question> call(String jsonString) {
                        try {
                            return new QuestionResultListJsonMapper().fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Override public Observable<Answer> saveAnswer(Answer answer) {
        final AnswerJsonMapper jsonMapper = new AnswerJsonMapper();
        return Observable.just(answer)
                .map(new Func1<Answer, String>() {
                    @Override public String call(Answer answer) {
                        try {
                            return jsonMapper.toJsonString(answer);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(ANSWERS, json));
                    }
                })
                .map(new Func1<String, Answer>() {
                    @Override public Answer call(String json) {
                        try {
                            return jsonMapper.fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //     Methods for HTTP request
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Callable<String> createPOSTCallable(final String url, final String json) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                return createPOST(url, json);
            }
        };
    }

    private String createPOST(String url, String json) throws Exception {
        if (isThereInternetConnection()) {
            Request request = buildPOSTRequest(url, json);
            LOGD(TAG, request.toString());
            Response response = mOkHttpClient.newCall(request).execute();
            try (ResponseBody body = response.body()) {
                return body.string();
            }
        } else {
            throw new NetworkConnectionException("There is no internet connection.");
        }
    }

    private Request buildPOSTRequest(String url, String json) throws Exception {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    private Callable<String> createGETCallable(final String url) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                return createGET(url);
            }
        };
    }

    private String createGET(String url) throws Exception {
        if (isThereInternetConnection()) {
            Request request = buildGETRequest(url);
            LOGD(TAG, request.toString());
            Response response = mOkHttpClient.newCall(request).execute();
            try (ResponseBody body = response.body()) {
                return body.string();
            }
        } else {
            throw new NetworkConnectionException("There is no internet connection.");
        }
    }

    private Request buildGETRequest(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Callable<String> createPUTCallable(final String url, final String json) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                return createPUT(url, json);
            }
        };
    }

    private String createPUT(String url, String json) throws Exception {
        if (isThereInternetConnection()) {
            Request request = buildPUTRequest(url, json);
            LOGD(TAG, request.toString());
            Response response = mOkHttpClient.newCall(request).execute();
            try (ResponseBody body = response.body()) {
                return body.string();
            }
        } else {
            throw new NetworkConnectionException("There is no internet connection.");
        }
    }

    private Request buildPUTRequest(String url, String json) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        return new Request.Builder()
                .url(url)
                .put(body)
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
}
