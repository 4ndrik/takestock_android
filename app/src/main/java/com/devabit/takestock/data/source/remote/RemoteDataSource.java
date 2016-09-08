package com.devabit.takestock.data.source.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.filter.UserFilter;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.remote.filterBuilder.AdvertFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.filterBuilder.OfferFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.filterBuilder.QuestionFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.filterBuilder.UserFilterUrlBuilder;
import com.devabit.takestock.data.source.remote.mapper.*;
import com.devabit.takestock.exception.HttpResponseException;
import com.devabit.takestock.exception.NetworkConnectionException;
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

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static timber.log.Timber.d;

/**
 * Implementation for retrieving data from the network.
 * <p/>
 * Created by Victor Artemyev on 22/04/2016.
 */
public class RemoteDataSource implements ApiRest, DataSource {

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

    private final TakeStockAccount mAccount;
    private final ConnectivityManager mConnectivityManager;
    private final OkHttpClient mOkHttpClient;

    private RemoteDataSource(Context context) {
        mAccount = TakeStockAccount.get(context);
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

                Response response;
                String accessToken = mAccount.getAccessToken();
                if (!accessToken.isEmpty()) {
                    setAuthHeader(builder, accessToken);
                    request = builder.build();
                    response = chain.proceed(request);

                    if (response.code() == 401) { //if unauthorized
                        accessToken = refreshAccessToken(accessToken);
                        mAccount.setAccessToken(accessToken);
                        setAuthHeader(builder, accessToken);
                    }
                } else {
                    response = chain.proceed(request);
                }

                return checkResponsePerCode(response);
            }
        };
    }

    private String refreshAccessToken(String token) {
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

    /********* Entries Methods  ********/

    @Override public Observable<AuthToken> signUp(@NonNull final UserCredentials credentials) {
        return buildUserCredentialsAsJsonStringObservable(credentials)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(TOKEN_REGISTER, json));
                    }
                }).map(new Func1<String, AuthToken>() {
                    @Override public AuthToken call(String json) {
                        try {
                            d(json);
                            return new AuthTokenJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<AuthToken>() {
                    @Override public void call(AuthToken authToken) {
                        d(authToken.toString());
                        mAccount.createAccount(authToken, credentials.password);
                    }
                });
    }

    @Override public Observable<AuthToken> signIn(@NonNull final UserCredentials credentials) {
        return buildUserCredentialsAsJsonStringObservable(credentials)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        d(json);
                        return Observable.fromCallable(createPOSTCallable(TOKEN_AUTH, json));
                    }
                })
                .map(new Func1<String, AuthToken>() {
                    @Override public AuthToken call(String json) {
                        try {
                            d(json);
                            return new AuthTokenJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<AuthToken>() {
                    @Override public void call(AuthToken authToken) {
                        d(authToken.toString());
                        mAccount.createAccount(authToken, credentials.password);
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

    /********* Categories Methods  ********/

    @Override public Observable<List<Category>> saveCategories(@NonNull List<Category> categories) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> refreshCategories() {
        return getCategories();
    }

    @Override public Observable<List<Category>> getCategories() {
        return Observable.fromCallable(createGETCallable(CATEGORY))
                .map(new Func1<String, List<Category>>() {
                    @Override public List<Category> call(String json) {
                        try {
                            return new CategoryJsonMapper().fromJsonString(json);
                        } catch (Exception e) {
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

    /********* Sizes Methods  ********/

    @Override public Observable<List<Size>> saveSizes(@NonNull List<Size> sizes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> refreshSizes() {
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

    /********* Certifications Methods  ********/

    @Override public Observable<List<Certification>> saveCertifications(@NonNull List<Certification> certifications) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> refreshCertifications() {
        return getCertifications();
    }

    @Override public Observable<List<Certification>> getCertifications() {
        return Observable.fromCallable(createGETCallable(CERTIFICATIONS))
                .map(new Func1<String, List<Certification>>() {
                    @Override public List<Certification> call(String json) {
                        try {
                            return new CertificationJsonMapper().fromJsonStringToList(json);
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

    @Override public Certification getCertificationWithId(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********* Shippings Methods  ********/

    @Override public Observable<List<Shipping>> saveShippings(@NonNull List<Shipping> shippings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> refreshShippings() {
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

    @Override public Shipping getShippingWithId(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Condition
    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Packaging
    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////
    // Methods for OfferStatus
    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////
    // Methods for BusinessType
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveBusinessTypes(@NonNull List<BusinessType> businessTypes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<BusinessType>> updateBusinessTypes() {
        return getBusinessTypes();
    }

    @Override public Observable<List<BusinessType>> getBusinessTypes() {
        return Observable.fromCallable(createGETCallable(BUSINESS_TYPE))
                .map(new Func1<String, List<BusinessType>>() {
                    @Override public List<BusinessType> call(String json) {
                        try {
                            return new BusinessTypeJsonMapper().fromJsonStringToList(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<List<BusinessType>>() {
                    @Override public void call(List<BusinessType> types) {
                        LOGD(TAG, "BusinessTypes from RemoteDataSource " + types);
                    }
                });
    }

    @Override public BusinessType getBusinessTypeById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public BusinessSubtype getBusinessSubtypeById(int id) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Advert
    ///////////////////////////////////////////////////////////////////////////

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

    @Override public Observable<List<Advert>> getAdvertsWithFilter(@NonNull AdvertFilter filter) {
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
                                    AdvertPaginatedListJsonMapper jsonMapper = new AdvertPaginatedListJsonMapper();
                                    PaginatedList<Advert> paginatedList = jsonMapper.fromJsonString(createGET(page));
                                    List<Advert> result = new ArrayList<>(paginatedList.getResults());
                                    while (paginatedList.hasNext()) {
                                        String nextPage = paginatedList.getNext();
                                        paginatedList = jsonMapper.fromJsonString(createGET(nextPage));
                                        result.addAll(paginatedList.getResults());
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

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListWithFilter(@NonNull AdvertFilter filter) {
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
                .flatMap(new Func1<String, Observable<PaginatedList<Advert>>>() {
                    @Override public Observable<PaginatedList<Advert>> call(String url) {
                        return getAdvertResultListPerPage(url);
                    }
                });
    }

    @Override public Observable<PaginatedList<Advert>> getAdvertResultListPerPage(@NonNull String page) {
        return Observable.fromCallable(createGETCallable(page))
                .map(new Func1<String, PaginatedList<Advert>>() {
                    @Override public PaginatedList<Advert> call(String json) {
                        try {
                            LOGD(TAG, json);
                            return new AdvertPaginatedListJsonMapper().fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).doOnNext(new Action1<PaginatedList<Advert>>() {
                    @Override public void call(PaginatedList<Advert> advertPaginatedList) {
                        LOGD(TAG, "ResultAdvertList: " + advertPaginatedList);
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Offer AdvertSubscriber
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<AdvertSubscriber> addRemoveAdvertWatching(@NonNull AdvertSubscriber subscriber) {
        final AdvertSubscriberJsonMapper jsonMapper = new AdvertSubscriberJsonMapper();
        return Observable.just(subscriber)
                .map(new Func1<AdvertSubscriber, String>() {
                    @Override public String call(AdvertSubscriber subscriber) {
                        try {
                            return jsonMapper.toJsonString(subscriber);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPOSTCallable(WATCHLIST, json));
                    }
                })
                .map(new Func1<String, AdvertSubscriber>() {
                    @Override public AdvertSubscriber call(String json) {
                        try {
                            return jsonMapper.fromJsonString(json);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Offer
    ///////////////////////////////////////////////////////////////////////////

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
                                    OfferPaginatedListJsonMapper jsonMapper = new OfferPaginatedListJsonMapper();
                                    PaginatedList<Offer> paginatedList = jsonMapper.fromJsonString(createGET(page));
                                    List<Offer> result = new ArrayList<>(paginatedList.getResults());
                                    while (paginatedList.hasNext()) {
                                        String nextPage = paginatedList.getNext();
                                        paginatedList = jsonMapper.fromJsonString(createGET(nextPage));
                                        result.addAll(paginatedList.getResults());
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

    @Override public Observable<PaginatedList<Offer>> getOfferResultListPerFilter(@NonNull OfferFilter filter) {
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
                .flatMap(new Func1<String, Observable<PaginatedList<Offer>>>() {
                    @Override public Observable<PaginatedList<Offer>> call(String url) {
                        return getOfferResultListPerPage(url);
                    }
                });
    }

    @Override public Observable<PaginatedList<Offer>> getOfferResultListPerPage(@NonNull String page) {
        return Observable.fromCallable(createGETCallable(page))
                .map(new Func1<String, PaginatedList<Offer>>() {
                    @Override public PaginatedList<Offer> call(String jsonString) {
                        try {
                            LOGD(TAG, jsonString);
                            return new OfferPaginatedListJsonMapper().fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Question
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Question> saveQuestion(@NonNull Question question) {
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

    @Override public Observable<PaginatedList<Question>> getQuestionResultListPerFilter(@NonNull QuestionFilter filter) {
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
                .flatMap(new Func1<String, Observable<PaginatedList<Question>>>() {
                    @Override public Observable<PaginatedList<Question>> call(String page) {
                        return getQuestionResultListPerPage(page);
                    }
                });
    }

    @Override public Observable<PaginatedList<Question>> getQuestionResultListPerPage(@NonNull String page) {
        return Observable.fromCallable(createGETCallable(page))
                .map(new Func1<String, PaginatedList<Question>>() {
                    @Override public PaginatedList<Question> call(String jsonString) {
                        try {
                            return new QuestionPaginatedListJsonMapper().fromJsonString(jsonString);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Answer
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Answer> saveAnswer(@NonNull Answer answer) {
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

    ///////////////////////////////////////////////////////////////////////////
    // Methods for User
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<User> saveUser(@NonNull User user) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<User> updateUser(@NonNull User user) {
        final UserJsonMapper jsonMapper = new UserJsonMapper();
        return Observable.just(user)
                .map(new Func1<User, String>() {
                    @Override public String call(User user) {
                        try {
                            return jsonMapper.toJsonString(user);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        return Observable.fromCallable(createPATCHCallable(ME, json));
                    }
                })
                .map(new Func1<String, User>() {
                    @Override public User call(String json) {
                        LOGD(TAG, json);
                        try {
                            return jsonMapper.fromJsonString(json);
                        } catch (JSONException e) {
                            throw new RuntimeException(json);
                        }
                    }
                });
    }

    @Override public Observable<List<User>> getUsersPerFilter(@NonNull UserFilter filter) {
        return Observable.just(filter)
                .map(new Func1<UserFilter, String>() {
                    @Override public String call(UserFilter userFilter) {
                        return new UserFilterUrlBuilder(USERS, userFilter).buildUrl();
                    }
                })
                .flatMap(new Func1<String, Observable<List<User>>>() {
                    @Override public Observable<List<User>> call(final String page) {
                        return Observable.create(new Observable.OnSubscribe<List<User>>() {
                            @Override public void call(Subscriber<? super List<User>> subscriber) {
                                try {
                                    UserPaginatedListJsonMapper jsonMapper = new UserPaginatedListJsonMapper();
                                    PaginatedList<User> paginatedList = jsonMapper.fromJsonString(createGET(page));
                                    List<User> result = new ArrayList<>(paginatedList.getResults());
                                    while (paginatedList.hasNext()) {
                                        String nextPage = paginatedList.getNext();
                                        paginatedList = jsonMapper.fromJsonString(createGET(nextPage));
                                        result.addAll(paginatedList.getResults());
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

    @Override public Observable<PaginatedList<User>> getUserResultListPerFilter(@NonNull UserFilter filter) {
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Payment
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<String> addPayment(@NonNull Payment payment) {
        return Observable.just(payment)
                .map(new Func1<Payment, String>() {
                    @Override public String call(Payment payment) {
                        try {
                            return new PaymentJsonMapper().toJsonString(payment);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override public Observable<String> call(String json) {
                        LOGD(TAG, "Payment json: " + json);
                        return Observable.fromCallable(createPOSTCallable(PAY, json));
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for HTTP request
    ///////////////////////////////////////////////////////////////////////////

    /**
     * POST
     */
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

    /**
     * GET
     */
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

    /**
     * PUT
     */
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
     * PATCH
     */
    private Callable<String> createPATCHCallable(final String url, final String json) {
        return new Callable<String>() {
            @Override public String call() throws Exception {
                return createPATCH(url, json);
            }
        };
    }

    private String createPATCH(String url, String json) throws Exception {
        if (isThereInternetConnection()) {
            Request request = buildPATCHRequest(url, json);
            LOGD(TAG, request.toString());
            Response response = mOkHttpClient.newCall(request).execute();
            try (ResponseBody body = response.body()) {
                return body.string();
            }
        } else {
            throw new NetworkConnectionException("There is no internet connection.");
        }
    }

    private Request buildPATCHRequest(String url, String json) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        return new Request.Builder()
                .url(url)
                .patch(body)
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
