package com.devabit.takestock.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.filter.UserFilter;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.local.dao.*;
import com.devabit.takestock.data.source.local.entity.*;
import com.devabit.takestock.data.source.local.filterBuilders.AdvertFilterQueryBuilder;
import com.devabit.takestock.data.source.local.filterBuilders.UserFilterQueryBuilder;
import com.devabit.takestock.data.source.local.mapper.*;
import io.realm.*;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;
import java.util.concurrent.Callable;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;

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

    private final RealmConfiguration mRealmConfiguration;

    private LocalDataSource(Context context) {
        mRealmConfiguration = buildRealmConfiguration(context);
        Realm.setDefaultConfiguration(mRealmConfiguration);
    }

    private RealmConfiguration buildRealmConfiguration(Context context) {
        return new RealmConfiguration.Builder(context)
                .name("takestock.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    /********** Entries Methods ********/

    @Override public Observable<AuthToken> signUp(@NonNull UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<AuthToken> signIn(@NonNull UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********** Categories Methods ********/

    @Override public Observable<List<Category>> saveCategories(@NonNull List<Category> categories) {
        return Observable.just(categories)
                .doOnNext(new Action1<List<Category>>() {
                    @Override public void call(List<Category> categories) {
                        CategoryRealmDao dao = new CategoryRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateCategoryList(categories);
                    }
                });
    }

    @Override public Observable<List<Category>> refreshCategories() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> getCategories() {
        return Observable.fromCallable(new Callable<List<Category>>() {
            @Override public List<Category> call() throws Exception {
                CategoryRealmDao dao = new CategoryRealmDao(mRealmConfiguration);
                return dao.getCategoryList();
            }
        });
    }

    /********** Sizes Methods ********/

    @Override public Observable<List<Size>> saveSizes(@NonNull List<Size> sizes) {
        return Observable.just(sizes)
                .doOnNext(new Action1<List<Size>>() {
                    @Override public void call(List<Size> sizes) {
                        SizeRealmDao dao = new SizeRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateSizeList(sizes);
                    }
                });
    }

    @Override public Observable<List<Size>> refreshSizes() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        return Observable.fromCallable(new Callable<List<Size>>() {
            @Override public List<Size> call() throws Exception {
                SizeRealmDao dao = new SizeRealmDao(mRealmConfiguration);
                return dao.getSizeList();
            }
        });
    }

    /********** Certifications Methods ********/

    @Override public Observable<List<Certification>> saveCertifications(@NonNull List<Certification> certifications) {
        return Observable.just(certifications)
                .doOnNext(new Action1<List<Certification>>() {
                    @Override public void call(List<Certification> certifications) {
                        CertificationRealmDao dao = new CertificationRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateCertificationList(certifications);
                    }
                });
    }

    @Override public Observable<List<Certification>> refreshCertifications() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> getCertifications() {
        return Observable.fromCallable(new Callable<List<Certification>>() {
            @Override public List<Certification> call() throws Exception {
                CertificationRealmDao dao = new CertificationRealmDao(mRealmConfiguration);
                return dao.getCertificationList();
            }
        });
    }

    @Override public Certification getCertificationWithId(int id) {
        return new CertificationRealmDao(mRealmConfiguration).getCertificationWithId(id);
    }

    /********** Shippings Methods ********/

    @Override public Observable<List<Shipping>> saveShippings(@NonNull List<Shipping> shippings) {
        return Observable.just(shippings)
                .doOnNext(new Action1<List<Shipping>>() {
                    @Override public void call(List<Shipping> shippings) {
                        ShippingRealmDao dao = new ShippingRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateShippingList(shippings);
                    }
                });
    }

    @Override public Observable<List<Shipping>> refreshShippings() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShippings() {
        return Observable.fromCallable(new Callable<List<Shipping>>() {
            @Override public List<Shipping> call() throws Exception {
                ShippingRealmDao dao = new ShippingRealmDao(mRealmConfiguration);
                return dao.getShippingList();
            }
        });
    }

    @Override public Shipping getShippingWithId(int id) {
        return new ShippingRealmDao(mRealmConfiguration).getShippingWithId(id);
    }

    /********** Conditions Methods ********/

    @Override public Observable<List<Condition>> saveConditions(@NonNull List<Condition> conditionList) {
       return Observable.just(conditionList)
               .doOnNext(new Action1<List<Condition>>() {
                   @Override public void call(List<Condition> conditionList) {
                       ConditionRealmDao dao = new ConditionRealmDao(mRealmConfiguration);
                       dao.storeOrUpdateConditionList(conditionList);
                   }
               });
    }

    @Override public Observable<List<Condition>> refreshConditions() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        return Observable.fromCallable(new Callable<List<Condition>>() {
            @Override public List<Condition> call() throws Exception {
                ConditionRealmDao dao = new ConditionRealmDao(mRealmConfiguration);
                return dao.getConditionList();
            }
        });
    }

    @Override public Condition getConditionWithId(int id) {
        return new ConditionRealmDao(mRealmConfiguration).getConditionWithId(id);
    }

    /********** Packagings Methods ********/

    @Override public Observable<List<Packaging>> savePackagings(@NonNull List<Packaging> packagings) {
        return Observable.just(packagings)
                .doOnNext(new Action1<List<Packaging>>() {
                    @Override public void call(List<Packaging> packagings) {
                        PackagingRealmDao dao = new PackagingRealmDao(mRealmConfiguration);
                        dao.storeOrUpdatePackagingList(packagings);
                    }
                });
    }

    @Override public Observable<List<Packaging>> refreshPackagings() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Packaging>> getPackagings() {
        return Observable.fromCallable(new Callable<List<Packaging>>() {
            @Override public List<Packaging> call() throws Exception {
                PackagingRealmDao dao = new PackagingRealmDao(mRealmConfiguration);
                return dao.getPackagingList();
            }
        });
    }

    @Override public Packaging getPackagingWithId(int id) {
        return new PackagingRealmDao(mRealmConfiguration).getPackagingWithId(id);
    }

    /********* OfferStatuses Methods  ********/

    @Override public Observable<List<OfferStatus>> saveOfferStatuses(@NonNull List<OfferStatus> statuses) {
        return Observable.just(statuses)
                .doOnNext(new Action1<List<OfferStatus>>() {
                    @Override public void call(List<OfferStatus> offerStatuses) {
                        OfferStatusRealmDao dao = new OfferStatusRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateOfferStatusList(offerStatuses);
                    }
                });
    }

    @Override public Observable<List<OfferStatus>> refreshOfferStatuses() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<OfferStatus>> getOfferStatuses() {
        return Observable.fromCallable(new Callable<List<OfferStatus>>() {
            @Override public List<OfferStatus> call() throws Exception {
                OfferStatusRealmDao dao = new OfferStatusRealmDao(mRealmConfiguration);
                return dao.getOfferStatusList();
            }
        });
    }

    @Override public OfferStatus getOfferStatusWithId(int id) {
        return new OfferStatusRealmDao(mRealmConfiguration).getOfferStatusWithId(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for BusinessType
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveBusinessTypes(@NonNull List<BusinessType> businessTypes) {
        BusinessTypeDataMapper dataMapper = new BusinessTypeDataMapper();
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            for (BusinessType type : businessTypes) {
                BusinessTypeEntity typeEntity = dataMapper.transformToEntity(type);
                typeEntity = realm.copyToRealmOrUpdate(typeEntity);
                RealmList<BusinessSubtypeEntity> subcategoryEntities = typeEntity.getSubtypeEntities();
                for (BusinessSubtype subtype : type.getSubtypes()) {
                    BusinessSubtypeEntity subtypeEntity = dataMapper.transformSubtypeToEntity(subtype);
                    subcategoryEntities.add(subtypeEntity);
                }
            }
            realm.commitTransaction();
        }
    }

    @Override public Observable<List<BusinessType>> updateBusinessTypes() {
        return null;
    }

    @Override public Observable<List<BusinessType>> getBusinessTypes() {
        return Observable.fromCallable(new Callable<RealmResults<BusinessTypeEntity>>() {
            @Override public RealmResults<BusinessTypeEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(BusinessTypeEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<BusinessTypeEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<BusinessTypeEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<BusinessTypeEntity>, List<BusinessType>>() {
            @Override public List<BusinessType> call(RealmResults<BusinessTypeEntity> entities) {
                return new BusinessTypeDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<BusinessType>>() {
            @Override public void call(List<BusinessType> types) {
                LOGD(TAG, "BusinessTypes from LocalDataSource " + types);
            }
        });
    }

    @Override public BusinessType getBusinessTypeById(int id) {
        BusinessTypeEntity entity = Realm.getDefaultInstance()
                .where(BusinessTypeEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new BusinessTypeDataMapper().transformFromEntity(entity);
    }

    @Override public BusinessSubtype getBusinessSubtypeById(int id) {
        BusinessSubtypeEntity entity = Realm.getDefaultInstance()
                .where(BusinessSubtypeEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new BusinessSubtypeDataMapper().transformFromEntity(entity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Advert
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Advert> saveAdvert(@NonNull Advert advert) {
        return Observable.just(advert)
                .doOnNext(new Action1<Advert>() {
                    @Override public void call(Advert advert) {
                        try (Realm realm = Realm.getDefaultInstance()) {
                            realm.beginTransaction();
                            AdvertEntityDataMapper advertMapper = new AdvertEntityDataMapper();
                            AdvertEntity advertEntity = advertMapper.transformToEntity(advert);
                            advertEntity = realm.copyToRealmOrUpdate(advertEntity);
                            for (String tag : advert.getTags()) {
                                StringEntity tagEntity = advertMapper.transformTagToEntity(tag);
                                advertEntity.addTag(tagEntity);
                            }
                            for (Photo photo : advert.getPhotos()) {
                                PhotoEntity photoEntity = advertMapper.transformPhotoToEntity(photo);
                                advertEntity.addPhoto(photoEntity);
                            }
                            realm.commitTransaction();
                        }
                    }
                });
    }

    @Override public Observable<List<Advert>> getAdvertsWithFilter(@NonNull AdvertFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListPerPage(@NonNull String link) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert.Subscriber> addRemoveAdvertWatching(int advertId) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListWithFilter(@NonNull AdvertFilter filter) {
        return Observable.just(filter)
                .map(new Func1<AdvertFilter, RealmResults<AdvertEntity>>() {
                    @Override public RealmResults<AdvertEntity> call(AdvertFilter advertFilter) {
                        RealmQuery<AdvertEntity> query = new AdvertFilterQueryBuilder().buildQuery(advertFilter);
                        return query.findAll();
                    }
                })
                .map(new Func1<RealmResults<AdvertEntity>, List<Advert>>() {
                    @Override public List<Advert> call(RealmResults<AdvertEntity> advertEntities) {
                        return new AdvertEntityDataMapper().transformFromEntitiesToList(advertEntities);
                    }
                }).map(new Func1<List<Advert>, PaginatedList<Advert>>() {
                    @Override public PaginatedList<Advert> call(List<Advert> adverts) {
                        PaginatedList<Advert> paginatedList = new PaginatedList<>();
                        paginatedList.setResults(adverts);
                        return paginatedList;
                    }
                });

    }

    /********* Offers Methods  ********/

    @Override public Observable<Offer> makeOffer(@NonNull Offer offer) {
        return Observable.just(offer)
                .doOnNext(new Action1<Offer>() {
                    @Override public void call(Offer offer) {
                        try (Realm realm = Realm.getDefaultInstance()) {
                            realm.beginTransaction();
                            OfferEntity entity = new OfferEntityDataMapper().transformToEntity(offer);
                            realm.copyToRealmOrUpdate(entity);
                            realm.commitTransaction();
                        }
                    }
                });
    }

    @Override public Observable<Offer> updateOffer(@NonNull Offer offer) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Offer>> getOffersPerFilter(@NonNull OfferFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Offer>> getPaginatedOfferListWithFilter(@NonNull OfferFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Offer>> getPaginatedOfferListPerPage(@NonNull String page) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********* Questions Methods  ********/

    @Override public Observable<Question> saveQuestion(@NonNull Question question) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Question>> getPaginatedQuestionListWithFilter(@NonNull QuestionFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Question>> getPaginatedQuestionListPerPage(@NonNull String page) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Answer
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Answer> saveAnswer(@NonNull Answer answer) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for User
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<User> saveUser(@NonNull User user) {
        return Observable.just(user)
                .doOnNext(new Action1<User>() {
                    @Override public void call(User user) {
                        try (Realm realm = Realm.getDefaultInstance()) {
                            realm.beginTransaction();
                            UserEntity entity = new UserEntityDataMapper().transformToEntity(user);
                            realm.copyToRealmOrUpdate(entity);
                            realm.commitTransaction();
                        }
                    }
                });
    }

    @Override public Observable<User> updateUser(@NonNull User user) {
        return saveUser(user);
    }

    @Override public Observable<List<User>> getUsersPerFilter(@NonNull UserFilter filter) {
        return Observable.just(filter)
                .map(new Func1<UserFilter, RealmResults<UserEntity>>() {
                    @Override public RealmResults<UserEntity> call(UserFilter userFilter) {
                        RealmQuery<UserEntity> query = new UserFilterQueryBuilder().buildQuery(userFilter);
                        return query.findAll();
                    }
                })
                .filter(new Func1<RealmResults<UserEntity>, Boolean>() {
                    @Override public Boolean call(RealmResults<UserEntity> userEntities) {
                        return !userEntities.isEmpty();
                    }
                })
                .map(new Func1<RealmResults<UserEntity>, List<User>>() {
                    @Override public List<User> call(RealmResults<UserEntity> userEntities) {
                        return new UserEntityDataMapper().transformFromEntitiesToList(userEntities);
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
        throw new UnsupportedOperationException("This operation not required.");
    }

    private <E extends RealmModel> List<E> saveOrUpdateEntities(Iterable<E> objects) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            List<E> result = realm.copyToRealmOrUpdate(objects);
            realm.commitTransaction();
            return result;
        }
    }

    private <E extends RealmModel> E saveOrUpdateEntity(E object) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            E result = realm.copyToRealmOrUpdate(object);
            realm.commitTransaction();
            return result;
        }
    }
}
