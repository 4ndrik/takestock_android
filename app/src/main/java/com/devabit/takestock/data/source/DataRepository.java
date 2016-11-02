package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.model.*;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.List;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class DataRepository implements DataSource {

    private static DataRepository sInstance;

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @param localDataSource  the device storage data source
     * @return the {@link DataRepository} instance
     */
    public static DataRepository getInstance(DataSource remoteDataSource,
                                             DataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new DataRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;

    private DataRepository(@NonNull DataSource remoteDataSource,
                           @NonNull DataSource localDataSource) {
        mRemoteDataSource = checkNotNull(remoteDataSource);
        mLocalDataSource = checkNotNull(localDataSource);
    }

    /**
     * Used to force {@link #getInstance(DataSource, DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        sInstance = null;
    }

    /*********
     * Entries Methods
     ********/

    @Override public Observable<Authentication> signUp(@NonNull UserCredentials credentials) {
        return mRemoteDataSource.signUp(credentials)
                .flatMap(new Func1<Authentication, Observable<Authentication>>() {
                    @Override public Observable<Authentication> call(Authentication auth) {
                        return Observable.zip(
                                Observable.just(auth),
                                refreshAccountUserWithId(auth.getUserId()),
                                new Func2<Authentication, User, Authentication>() {
                                    @Override public Authentication call(Authentication authentication, User user) {
                                        authentication.setUser(user);
                                        return authentication;
                                    }
                                });
                    }
                });
    }

    @Override public Observable<Authentication> signIn(@NonNull UserCredentials credentials) {
        return mRemoteDataSource.signIn(credentials)
                .flatMap(new Func1<Authentication, Observable<Authentication>>() {
                    @Override public Observable<Authentication> call(Authentication authentication) {
                        return Observable.zip(
                                Observable.just(authentication),
                                mLocalDataSource.saveUser(authentication.getUser()),
                                new Func2<Authentication, User, Authentication>() {
                                    @Override public Authentication call(Authentication authentication, User user) {
                                        return authentication;
                                    }
                                });
                    }
                });
    }

    /*********
     * Categories Methods
     ********/

    @Override public Observable<List<Category>> saveCategories(@NonNull List<Category> categories) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> getCategories() {
        Observable<List<Category>> localCategories = mLocalDataSource.getCategories();
        Observable<List<Category>> remoteCategories = refreshCategories();
        return Observable.concat(localCategories, remoteCategories).first();
    }

    @Override public Observable<List<Category>> refreshCategories() {
        return mRemoteDataSource.refreshCategories()
                .flatMap(new Func1<List<Category>, Observable<List<Category>>>() {
                    @Override public Observable<List<Category>> call(List<Category> categories) {
                        return mLocalDataSource.saveCategories(categories);
                    }
                });
    }

    /*********
     * Sizes Methods
     ********/

    @Override public Observable<List<Size>> saveSizes(@NonNull List<Size> sizes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        Observable<List<Size>> localSizes = mLocalDataSource.getSizes();
        Observable<List<Size>> remoteSizes = refreshSizes();
        return Observable.concat(localSizes, remoteSizes).first();
    }

    @Override public Observable<List<Size>> refreshSizes() {
        return mRemoteDataSource.refreshSizes()
                .flatMap(new Func1<List<Size>, Observable<List<Size>>>() {
                    @Override public Observable<List<Size>> call(List<Size> sizes) {
                        return mLocalDataSource.saveSizes(sizes);
                    }
                });
    }

    /*********
     * Certifications Methods
     ********/

    @Override public Observable<List<Certification>> saveCertifications(@NonNull List<Certification> certifications) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> getCertifications() {
        Observable<List<Certification>> localCertifications = mLocalDataSource.getCertifications();
        Observable<List<Certification>> remoteCertifications = refreshCertifications();
        return Observable.concat(localCertifications, remoteCertifications).first();
    }

    @Override public Observable<List<Certification>> refreshCertifications() {
        return mRemoteDataSource.refreshCertifications()
                .flatMap(new Func1<List<Certification>, Observable<List<Certification>>>() {
                    @Override public Observable<List<Certification>> call(List<Certification> certifications) {
                        return mLocalDataSource.saveCertifications(certifications);
                    }
                });
    }

    @Override public Certification getCertificationWithId(int id) {
        return mLocalDataSource.getCertificationWithId(id);
    }

    /**********
     * Shipping Methods
     ********/

    @Override public Observable<List<Shipping>> saveShippings(@NonNull List<Shipping> shippings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShippings() {
        Observable<List<Shipping>> localShippings = mLocalDataSource.getShippings();
        Observable<List<Shipping>> remoteShippings = refreshShippings();
        return Observable.concat(localShippings, remoteShippings).first();
    }

    @Override public Observable<List<Shipping>> refreshShippings() {
        return mRemoteDataSource.refreshShippings()
                .flatMap(new Func1<List<Shipping>, Observable<List<Shipping>>>() {
                    @Override public Observable<List<Shipping>> call(List<Shipping> shippings) {
                        return mLocalDataSource.saveShippings(shippings);
                    }
                });
    }

    @Override public Shipping getShippingWithId(int id) {
        return mLocalDataSource.getShippingWithId(id);
    }

    /**********
     * Conditions Methods
     ********/

    @Override public Observable<List<Condition>> saveConditions(@NonNull List<Condition> conditions) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        Observable<List<Condition>> localConditions = mLocalDataSource.getConditions();
        Observable<List<Condition>> remoteConditions = refreshConditions();
        return Observable.concat(localConditions, remoteConditions).first();
    }

    @Override public Observable<List<Condition>> refreshConditions() {
        return mRemoteDataSource.refreshConditions()
                .flatMap(new Func1<List<Condition>, Observable<List<Condition>>>() {
                    @Override public Observable<List<Condition>> call(List<Condition> conditionList) {
                        return mLocalDataSource.saveConditions(conditionList);
                    }
                });
    }

    @Override public Condition getConditionWithId(int id) {
        return mLocalDataSource.getConditionWithId(id);
    }

    /**********
     * Packagings Methods
     ********/

    @Override public Observable<List<Packaging>> savePackagings(@NonNull List<Packaging> packagings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Packaging>> getPackagings() {
        Observable<List<Packaging>> localPackagings = mLocalDataSource.getPackagings();
        Observable<List<Packaging>> remotePackagings = refreshPackagings();
        return Observable.concat(localPackagings, remotePackagings).first();
    }

    @Override public Observable<List<Packaging>> refreshPackagings() {
        return mRemoteDataSource.refreshPackagings()
                .flatMap(new Func1<List<Packaging>, Observable<List<Packaging>>>() {
                    @Override public Observable<List<Packaging>> call(List<Packaging> packagings) {
                        return mLocalDataSource.savePackagings(packagings);
                    }
                });
    }

    @Override public Packaging getPackagingWithId(int id) {
        return mLocalDataSource.getPackagingWithId(id);
    }

    /*********
     * OfferStatuses Methods
     ********/

    @Override public Observable<List<OfferStatus>> saveOfferStatuses(@NonNull List<OfferStatus> statuses) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<OfferStatus>> refreshOfferStatuses() {
        return mRemoteDataSource.refreshOfferStatuses()
                .flatMap(new Func1<List<OfferStatus>, Observable<List<OfferStatus>>>() {
                    @Override public Observable<List<OfferStatus>> call(List<OfferStatus> offerStatuses) {
                        return mLocalDataSource.saveOfferStatuses(offerStatuses);
                    }
                });
    }

    @Override public Observable<List<OfferStatus>> getOfferStatuses() {
        Observable<List<OfferStatus>> localStatuses = mLocalDataSource.getOfferStatuses();
        Observable<List<OfferStatus>> remoteStatuses = refreshOfferStatuses();
        return Observable.concat(localStatuses, remoteStatuses).first();
    }

    @Override public OfferStatus getOfferStatusWithId(int id) {
        return mLocalDataSource.getOfferStatusWithId(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for BusinessType
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveBusinessTypes(@NonNull List<BusinessType> businessTypes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<BusinessType>> updateBusinessTypes() {
        return mRemoteDataSource.updateBusinessTypes()
                .doOnNext(new Action1<List<BusinessType>>() {
                    @Override public void call(List<BusinessType> businessTypes) {
                        mLocalDataSource.saveBusinessTypes(businessTypes);
                    }
                });
    }

    @Override public Observable<List<BusinessType>> getBusinessTypes() {
        Observable<List<BusinessType>> localBusinessType = mLocalDataSource.getBusinessTypes();
        Observable<List<BusinessType>> remoteBusinessType = updateBusinessTypes();
        return Observable.concat(localBusinessType, remoteBusinessType).first();
    }

    @Override public BusinessType getBusinessTypeById(int id) {
        return mLocalDataSource.getBusinessTypeById(id);
    }

    @Override public BusinessSubtype getBusinessSubtypeById(int id) {
        return mLocalDataSource.getBusinessSubtypeById(id);
    }

    /*********
     * Adverts Methods
     ********/

    @Override public Observable<Advert> saveAdvert(@NonNull Advert advert) {
        return mRemoteDataSource.saveAdvert(advert);
    }

    @Override public Observable<Advert> editAdvert(@NonNull Advert advert) {
        return mRemoteDataSource.editAdvert(advert);
    }

    @Override public Observable<Advert> getAdvertWithId(int advertId) {
        return mRemoteDataSource.getAdvertWithId(advertId);
    }

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListWithFilter(@NonNull AdvertFilter filter) {
        return mRemoteDataSource.getPaginatedAdvertListWithFilter(filter);
    }

    @Override public Observable<PaginatedList<Advert>> getPaginatedAdvertListPerPage(@NonNull String page) {
        return mRemoteDataSource.getPaginatedAdvertListPerPage(page);
    }

    @Override public Observable<Advert.Subscriber> addRemoveAdvertWatching(int advertId) {
        return mRemoteDataSource.addRemoveAdvertWatching(advertId);
    }

    @Override public Observable<Advert> viewAdvertWithId(int advertId) {
        return mRemoteDataSource.viewAdvertWithId(advertId);
    }

    @Override public Observable<Advert> unnotifyAdvertWithId(int advertId) {
        return mRemoteDataSource.unnotifyAdvertWithId(advertId);
    }

    /*********
     * Offers Methods
     ********/

    @Override public Observable<Offer> makeOffer(@NonNull Offer offer) {
        return mRemoteDataSource.makeOffer(offer);
    }

    @Override public Observable<Offer.Accept> acceptOffer(@NonNull Offer.Accept accept) {
        return mRemoteDataSource.acceptOffer(accept);
    }

    @Override public Observable<Offer> getOfferWithId(int offerId) {
        return mRemoteDataSource.getOfferWithId(offerId);
    }

    @Override public Observable<PaginatedList<Offer>> getPaginatedOfferListWithFilter(@NonNull OfferFilter filter) {
        return mRemoteDataSource.getPaginatedOfferListWithFilter(filter);
    }

    @Override public Observable<PaginatedList<Offer>> getPaginatedOfferListPerPage(@NonNull String page) {
        return mRemoteDataSource.getPaginatedOfferListPerPage(page);
    }

    /*********
     * Q&A Methods
     ********/

    @Override public Observable<Question> saveQuestion(@NonNull Question question) {
        return mRemoteDataSource.saveQuestion(question);
    }

    @Override public Observable<PaginatedList<Question>> getPaginatedQuestionListWithFilter(@NonNull QuestionFilter filter) {
        return mRemoteDataSource.getPaginatedQuestionListWithFilter(filter);
    }

    @Override public Observable<PaginatedList<Question>> getPaginatedQuestionListPerPage(@NonNull String page) {
        return mRemoteDataSource.getPaginatedQuestionListPerPage(page);
    }

    @Override public Observable<Answer> saveAnswer(@NonNull Answer answer) {
        return mRemoteDataSource.saveAnswer(answer);
    }

    /*********
     * User Methods
     ********/

    @Override public Observable<User> saveUser(@NonNull User user) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<User> updateUser(@NonNull User user) {
        return mRemoteDataSource.updateUser(user)
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override public Observable<User> call(User user) {
                        return mLocalDataSource.updateUser(user);
                    }
                });
    }

    @Override public Observable<User> refreshAccountUserWithId(int userId) {
        if (userId <= 0) return Observable.just(null);
        return mRemoteDataSource.refreshAccountUserWithId(userId)
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override public Observable<User> call(User user) {
                        return mLocalDataSource.updateUser(user);
                    }
                });
    }

    @Override public Observable<User> getAccountUserWithId(int userId) {
        return mLocalDataSource.getAccountUserWithId(userId);
    }

    @Override public Observable<User> getUserWithId(int id) {
        Observable<User> localUser = mLocalDataSource.getUserWithId(id);
        Observable<User> remoteUser = mRemoteDataSource.getUserWithId(id)
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override public Observable<User> call(User author) {
                        return mRemoteDataSource.saveUser(author);
                    }
                });
        return Observable.concat(localUser, remoteUser).first();
    }

    @Override public Observable<Boolean> changePassword(String currentPass, String newPass) {
        return mRemoteDataSource.changePassword(currentPass, newPass);
    }

    /**********
     * Payment Method
     **********/

    @Override public Observable<Payment> makePayment(@NonNull Payment payment) {
        return mRemoteDataSource.makePayment(payment);
    }


    /**********
     * Device Method
     **********/

    @Override public Observable<Boolean> registerDevice(@NonNull Device device) {
        return mRemoteDataSource.registerDevice(device);
    }

    @Override public Observable<Boolean> unregisterDevice(@NonNull String token) {
        return mRemoteDataSource.unregisterDevice(token);
    }


    /**********
     * Notification Methods
     **********/

    @Override public Observable<Notification> saveNotification(@NonNull Notification notification) {
        notification.setSaved(true);
        return mLocalDataSource.saveNotification(notification);
    }

    @Override public Observable<Notification> readNotification(@NonNull Notification notification) {
        return mLocalDataSource.readNotification(notification);
    }

    @Override public Observable<Notification> removeNotification(@NonNull Notification notification) {
        return mLocalDataSource.removeNotification(notification);
    }

    @Override public Observable<Integer> getNewNotificationsCount() {
        return mLocalDataSource.getNewNotificationsCount();
    }

    @Override public Observable<List<Notification>> getNotifications() {
        return mLocalDataSource.getNotifications();
    }

    @Override public Observable<Void> clearNotifications() {
        return mLocalDataSource.clearNotifications();
    }

    @Override public Observable<Boolean> sendInvite(@NonNull String email) {
        return mRemoteDataSource.sendInvite(email);
    }
}
