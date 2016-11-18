package com.devabit.takestock.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.local.dao.*;
import com.devabit.takestock.data.source.local.entity.BusinessSubtypeEntity;
import com.devabit.takestock.data.source.local.entity.BusinessTypeEntity;
import com.devabit.takestock.data.source.local.mapper.BusinessSubtypeDataMapper;
import com.devabit.takestock.data.source.local.mapper.BusinessTypeDataMapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Victor Artemyev on 28/04/2016.
 */
public class LocalDataSource implements DataSource {

    private static LocalDataSource sInstance;

    public static LocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocalDataSource(context);
        }
        return sInstance;
    }

    private final RealmConfiguration mRealmConfiguration;

    private LocalDataSource(Context context) {
        Realm.init(context);
        mRealmConfiguration = buildRealmConfiguration();
        Realm.setDefaultConfiguration(mRealmConfiguration);
    }

    private RealmConfiguration buildRealmConfiguration() {
        return new RealmConfiguration.Builder()
                .name("takestock.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    /********** Entries Methods ********/

    @Override public Observable<Authentication> signUp(@NonNull UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Authentication> signIn(@NonNull UserCredentials credentials) {
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
        Realm realm = Realm.getDefaultInstance();
        try {
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
        } finally {
            realm.close();
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
                Timber.d("BusinessTypes from LocalDataSource %s ", types);
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

    /********* Adverts Methods  ********/

    @Override public Observable<Advert> saveAdvert(@NonNull Advert advert) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert> editAdvert(@NonNull Advert advert) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert> getAdvertWithId(int advertId) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListPerPage(@NonNull String link) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert.Subscriber> addRemoveAdvertWatching(int advertId) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert> viewAdvertWithId(int advertId) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Advert> unnotifyAdvertWithId(int advertId) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListWithFilter(@NonNull AdvertFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********* Offers Methods  ********/

    @Override public Observable<Offer> makeOffer(@NonNull Offer offer) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Offer.Accept> acceptOffer(@NonNull Offer.Accept accept) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Offer> getOfferWithId(int offerId) {
        return null;
    }

    @Override public Observable<PaginatedList<Offer>> getPaginatedOfferListWithFilter(@NonNull OfferFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Offer>> getPaginatedOfferListPerPage(@NonNull String page) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********* Q&A Methods  ********/

    @Override public Observable<Question> saveQuestion(@NonNull Question question) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Question>> getPaginatedQuestionListWithFilter(@NonNull QuestionFilter filter) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<PaginatedList<Question>> getPaginatedQuestionListPerPage(@NonNull String page) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Answer> saveAnswer(@NonNull Answer answer) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********** User Methods **********/

    @Override public Observable<User> saveUser(@NonNull User user) {
        return updateUser(user);
    }

    @Override public Observable<User> updateUser(@NonNull User user) {
        return Observable.just(user)
                .doOnNext(new Action1<User>() {
                    @Override public void call(User user) {
                        UserRealmDao dao = new UserRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateUser(user);
                    }
                });
    }

    @Override public Observable<User> refreshAccountUserWithId(int userId) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<User> getAccountUserWithId(final int userId) {
        return Observable.fromCallable(new Callable<User>() {
            @Override public User call() throws Exception {
                UserRealmDao dao = new UserRealmDao(mRealmConfiguration);
                return dao.getUserWithId(userId);
            }
        });
    }

    @Override public Observable<User> getUserWithId(final int id) {
        return Observable.fromCallable(new Callable<User>() {
            @Override public User call() throws Exception {
                UserRealmDao dao = new UserRealmDao(mRealmConfiguration);
                return dao.getUserWithId(id);
            }
        });
    }

    @Override public Observable<Boolean> changePassword(String currentPass, String newPass) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Payment
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Payment> makePayment(@NonNull Payment payment) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Integer> getPaymentRate() {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Boolean> registerDevice(@NonNull Device device) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<Boolean> unregisterDevice(@NonNull String token) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    /********** Notification Methods **********/

    @Override public Observable<Notification> saveNotification(@NonNull Notification notification) {
        return Observable.just(notification)
                .doOnNext(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        NotificationRealmDao dao = new NotificationRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateNotification(notification);
                    }
                });
    }

    @Override public Observable<Notification> readNotification(@NonNull Notification notification) {
        return Observable.just(notification)
                .doOnNext(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        notification.setSaved(false);
                        NotificationRealmDao dao = new NotificationRealmDao(mRealmConfiguration);
                        dao.storeOrUpdateNotification(notification);
                    }
                });
    }

    @Override public Observable<Notification> removeNotification(@NonNull Notification notification) {
        return Observable.just(notification)
                .doOnNext(new Action1<Notification>() {
                    @Override public void call(Notification notification) {
                        NotificationRealmDao dao = new NotificationRealmDao(mRealmConfiguration);
                        dao.removeNotification(notification);
                    }
                });
    }

    @Override public Observable<Integer> getNewNotificationsCount() {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override public Integer call() throws Exception {
                NotificationRealmDao dao = new NotificationRealmDao(mRealmConfiguration);
                return (int) dao.getNewNotificationCount();
            }
        });
    }

    @Override public Observable<List<Notification>> getNotifications() {
        return Observable.fromCallable(new Callable<List<Notification>>() {
            @Override public List<Notification> call() throws Exception {
                NotificationRealmDao dao = new NotificationRealmDao(mRealmConfiguration);
                return dao.getNotificationList();
            }
        });
    }

    @Override public Observable<Void> clearNotifications() {
        return Observable.fromCallable(new Callable<Void>() {
            @Override public Void call() throws Exception {
                NotificationRealmDao dao = new NotificationRealmDao(mRealmConfiguration);
                dao.clearDatabase();
                return null;
            }
        });
    }

    @Override public Observable<Boolean> sendInvite(@NonNull String email) {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
