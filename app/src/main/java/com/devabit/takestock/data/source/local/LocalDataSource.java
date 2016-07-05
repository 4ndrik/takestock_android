package com.devabit.takestock.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.filter.UserFilter;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
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

    private static final String DATA_BASE_NAME = "takestock.realm";

    private LocalDataSource(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(DATA_BASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for AuthToken
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<AuthToken> obtainAuthTokenPerSignUp(@NonNull UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignIn(@NonNull UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Category
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveCategories(@NonNull List<Category> categories) {
        CategoryEntityDataMapper categoryMapper = new CategoryEntityDataMapper();
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            for (Category category : categories) {
                CategoryEntity categoryEntity = categoryMapper.transformToEntity(category);
                categoryEntity = realm.copyToRealmOrUpdate(categoryEntity);
                RealmList<SubcategoryEntity> subcategoryEntities = categoryEntity.getSubcategories();
                for (Subcategory subcategory : category.getSubcategories()) {
                    SubcategoryEntity subcategoryEntity = categoryMapper.transformSubcategoryToEntity(subcategory);
                    subcategoryEntities.add(subcategoryEntity);
                }
            }
            realm.commitTransaction();
        }
    }

    @Override public Observable<List<Category>> updateCategories() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> getCategories() {
        return Observable.fromCallable(new Callable<RealmResults<CategoryEntity>>() {
            @Override public RealmResults<CategoryEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(CategoryEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<CategoryEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<CategoryEntity> categories) {
                return !categories.isEmpty();
            }
        }).map(new Func1<RealmResults<CategoryEntity>, List<Category>>() {
            @Override public List<Category> call(RealmResults<CategoryEntity> entities) {
                return new CategoryEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Category>>() {
            @Override public void call(List<Category> categories) {
                LOGD(TAG, "Categories from LocalDataSource " + categories);
            }
        });
    }

    @Override public Category getCategoryById(int id) {
        CategoryEntity entity = Realm.getDefaultInstance()
                .where(CategoryEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new CategoryEntityDataMapper().transformFromEntity(entity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Size
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveSizes(@NonNull List<Size> sizes) {
        List<SizeEntity> entities = new SizeEntityDataMapper().transformToEntityList(sizes);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Size>> updateSizes() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        return Observable.fromCallable(new Callable<RealmResults<SizeEntity>>() {
            @Override public RealmResults<SizeEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(SizeEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<SizeEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<SizeEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<SizeEntity>, List<Size>>() {
            @Override public List<Size> call(RealmResults<SizeEntity> entities) {
                return new SizeEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Size>>() {
            @Override public void call(List<Size> sizes) {
                LOGD(TAG, "Sizes from LocalDataSource " + sizes);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Certification
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveCertifications(@NonNull List<Certification> certifications) {
        List<CertificationEntity> entities = new CertificationEntityDataMapper()
                .transformToEntityList(certifications);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Certification>> updateCertifications() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> getCertifications() {
        return Observable.fromCallable(new Callable<RealmResults<CertificationEntity>>() {
            @Override public RealmResults<CertificationEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(CertificationEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<CertificationEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<CertificationEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<CertificationEntity>, List<Certification>>() {
            @Override public List<Certification> call(RealmResults<CertificationEntity> entities) {
                return new CertificationEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Certification>>() {
            @Override public void call(List<Certification> certifications) {
                LOGD(TAG, "Certifications from LocalDataSource " + certifications);
            }
        });
    }

    @Override public Certification getCertificationById(int id) {
        CertificationEntity entity = Realm.getDefaultInstance()
                .where(CertificationEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new CertificationEntityDataMapper().transformFromEntity(entity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Shipping
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveShippings(@NonNull List<Shipping> shippings) {
        List<ShippingEntity> entities = new ShippingEntityDataMapper().transformToEntityList(shippings);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Shipping>> updateShippings() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShippings() {
        return Observable.fromCallable(new Callable<RealmResults<ShippingEntity>>() {
            @Override public RealmResults<ShippingEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(ShippingEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<ShippingEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<ShippingEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<ShippingEntity>, List<Shipping>>() {
            @Override public List<Shipping> call(RealmResults<ShippingEntity> entities) {
                return new ShippingEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Shipping>>() {
            @Override public void call(List<Shipping> shippings) {
                LOGD(TAG, "Shippings from LocalDataSource " + shippings);
            }
        });
    }

    @Override public Shipping getShippingById(int id) {
        ShippingEntity entity = Realm.getDefaultInstance()
                .where(ShippingEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new ShippingEntityDataMapper().transformFromEntity(entity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Condition
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveConditions(@NonNull List<Condition> conditionList) {
        List<ConditionEntity> entities = new ConditionEntityDataMapper()
                .transformToEntityList(conditionList);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Condition>> updateConditions() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        return Observable.fromCallable(new Callable<RealmResults<ConditionEntity>>() {
            @Override public RealmResults<ConditionEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(ConditionEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<ConditionEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<ConditionEntity> conditionEntities) {
                return !conditionEntities.isEmpty();
            }
        }).map(new Func1<RealmResults<ConditionEntity>, List<Condition>>() {
            @Override public List<Condition> call(RealmResults<ConditionEntity> conditionEntities) {
                return new ConditionEntityDataMapper().transformFromEntityList(conditionEntities);
            }
        }).doOnNext(new Action1<List<Condition>>() {
            @Override public void call(List<Condition> conditionList) {
                LOGD(TAG, "Conditions from LocalDataSource " + conditionList);
            }
        });
    }

    @Override public Condition getConditionById(int id) {
        ConditionEntity entity = Realm.getDefaultInstance()
                .where(ConditionEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new ConditionEntityDataMapper().transformFromEntity(entity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Packaging
    ///////////////////////////////////////////////////////////////////////////

    @Override public void savePackagings(@NonNull List<Packaging> packagings) {
        List<PackagingEntity> entities = new PackagingsEntityDataMaper().transformToEntityList(packagings);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Packaging>> updatePackagings() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Packaging>> getPackagings() {
        return Observable.fromCallable(new Callable<RealmResults<PackagingEntity>>() {
            @Override public RealmResults<PackagingEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(PackagingEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<PackagingEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<PackagingEntity> packagingEntities) {
                return !packagingEntities.isEmpty();
            }
        }).map(new Func1<RealmResults<PackagingEntity>, List<Packaging>>() {
            @Override public List<Packaging> call(RealmResults<PackagingEntity> packagingEntities) {
                return new PackagingsEntityDataMaper().transformFromEntityList(packagingEntities);
            }
        }).doOnNext(new Action1<List<Packaging>>() {
            @Override public void call(List<Packaging> packagingList) {
                LOGD(TAG, "Packagings from LocalDataSource " + packagingList);
            }
        });
    }

    @Override public Packaging getPackagingById(int id) {
        PackagingEntity entity = Realm.getDefaultInstance()
                .where(PackagingEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new PackagingsEntityDataMaper().transformFromEntity(entity);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for OfferStatus
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveOfferStatuses(@NonNull List<OfferStatus> statuses) {
        List<OfferStatusEntity> entities = new OfferStatusEntityDataMapper().transformToEntityList(statuses);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<OfferStatus>> updateOfferStatuses() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<OfferStatus>> getOfferStatuses() {
        return Observable.fromCallable(new Callable<RealmResults<OfferStatusEntity>>() {
            @Override public RealmResults<OfferStatusEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(OfferStatusEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<OfferStatusEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<OfferStatusEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<OfferStatusEntity>, List<OfferStatus>>() {
            @Override public List<OfferStatus> call(RealmResults<OfferStatusEntity> entities) {
                return new OfferStatusEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<OfferStatus>>() {
            @Override public void call(List<OfferStatus> statuses) {
                LOGD(TAG, "OfferStatuses from LocalDataSource " + statuses);
            }
        });
    }

    @Override public OfferStatus getOfferStatusById(int id) {
        OfferStatusEntity entity = Realm.getDefaultInstance()
                .where(OfferStatusEntity.class)
                .equalTo("mId", id)
                .findFirst();
        return new OfferStatusEntityDataMapper().transformFromEntity(entity);
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

    @Override public Observable<List<Advert>> getAdvertsPerFilter(@NonNull AdvertFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<ResultList<Advert>> getAdvertResultListPerPage(@NonNull String link) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<AdvertSubscriber> addRemoveAdvertWatching(@NonNull AdvertSubscriber subscriber) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<ResultList<Advert>> getAdvertResultListPerFilter(@NonNull AdvertFilter filter) {
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
                }).map(new Func1<List<Advert>, ResultList<Advert>>() {
                    @Override public ResultList<Advert> call(List<Advert> adverts) {
                        ResultList<Advert> resultList = new ResultList<>();
                        resultList.setResults(adverts);
                        return resultList;
                    }
                });

    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Offer
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Offer> saveOffer(@NonNull Offer offer) {
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

    @Override public Observable<ResultList<Offer>> getOfferResultListPerFilter(@NonNull OfferFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<ResultList<Offer>> getOfferResultListPerPage(@NonNull String page) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Question
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Question> saveQuestion(@NonNull Question question) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<ResultList<Question>> getQuestionResultListPerFilter(@NonNull QuestionFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<ResultList<Question>> getQuestionResultListPerPage(@NonNull String page) {
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

    @Override public Observable<ResultList<User>> getUserResultListPerFilter(@NonNull UserFilter filter) {
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
