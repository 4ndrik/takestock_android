package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.filters.OfferFilter;
import com.devabit.takestock.data.filters.QuestionFilter;
import com.devabit.takestock.data.filters.UserFilter;
import com.devabit.takestock.data.models.*;
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

    ///////////////////////////////////////////////////////////////////////////
    // Methods for AuthToken
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<AuthToken> obtainAuthTokenPerSignUp(@NonNull UserCredentials credentials) {
        return mRemoteDataSource.obtainAuthTokenPerSignUp(credentials);
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignIn(@NonNull UserCredentials credentials) {
        return mRemoteDataSource.obtainAuthTokenPerSignIn(credentials)
                .flatMap(new Func1<AuthToken, Observable<AuthToken>>() {
                    @Override public Observable<AuthToken> call(AuthToken authToken) {
                        return Observable.zip(Observable.just(authToken), mLocalDataSource.saveUser(authToken.user),
                                new Func2<AuthToken, User, AuthToken>() {
                                    @Override public AuthToken call(AuthToken authToken, User user) {
                                        return authToken;
                                    }
                                });
                    }
                });

    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Category
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveCategories(@NonNull List<Category> categories) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Category>> getCategories() {
        Observable<List<Category>> localCategories = mLocalDataSource.getCategories();
        Observable<List<Category>> remoteCategories = updateCategories();
        return Observable.concat(localCategories, remoteCategories).first();
    }

    @Override public Observable<List<Category>> updateCategories() {
        return mRemoteDataSource.updateCategories()
                .doOnNext(new Action1<List<Category>>() {
                    @Override public void call(List<Category> categories) {
                        mLocalDataSource.saveCategories(categories);
                    }
                });
    }

    @Override public Category getCategoryById(int id) {
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Size
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveSizes(@NonNull List<Size> sizes) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Size>> getSizes() {
        Observable<List<Size>> localSizes = mLocalDataSource.getSizes();
        Observable<List<Size>> remoteSizes = updateSizes();
        return Observable.concat(localSizes, remoteSizes).first();
    }

    @Override public Observable<List<Size>> updateSizes() {
        return mRemoteDataSource.updateSizes()
                .doOnNext(new Action1<List<Size>>() {
                    @Override public void call(List<Size> sizes) {
                        mLocalDataSource.saveSizes(sizes);
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Certification
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveCertifications(@NonNull List<Certification> certifications) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Certification>> getCertifications() {
        Observable<List<Certification>> localCertifications = mLocalDataSource.getCertifications();
        Observable<List<Certification>> remoteCertifications = updateCertifications();
        return Observable.concat(localCertifications, remoteCertifications).first();
    }

    @Override public Observable<List<Certification>> updateCertifications() {
        return mRemoteDataSource.updateCertifications()
                .doOnNext(new Action1<List<Certification>>() {
                    @Override public void call(List<Certification> certifications) {
                        mLocalDataSource.saveCertifications(certifications);
                    }
                });
    }

    @Override public Certification getCertificationById(int id) {
        return mLocalDataSource.getCertificationById(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Shipping
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveShippings(@NonNull List<Shipping> shippings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Shipping>> getShippings() {
        Observable<List<Shipping>> localShippings = mLocalDataSource.getShippings();
        Observable<List<Shipping>> remoteShippings = updateShippings();
        return Observable.concat(localShippings, remoteShippings).first();
    }

    @Override public Observable<List<Shipping>> updateShippings() {
        return mRemoteDataSource.updateShippings()
                .doOnNext(new Action1<List<Shipping>>() {
                    @Override public void call(List<Shipping> shippings) {
                        mLocalDataSource.saveShippings(shippings);
                    }
                });
    }

    @Override public Shipping getShippingById(int id) {
        return mLocalDataSource.getShippingById(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Condition
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveConditions(@NonNull List<Condition> conditions) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Condition>> getConditions() {
        Observable<List<Condition>> localConditions = mLocalDataSource.getConditions();
        Observable<List<Condition>> remoteConditions = updateConditions();
        return Observable.concat(localConditions, remoteConditions).first();
    }

    @Override public Observable<List<Condition>> updateConditions() {
        return mRemoteDataSource.updateConditions()
                .doOnNext(new Action1<List<Condition>>() {
                    @Override public void call(List<Condition> conditions) {
                        mLocalDataSource.saveConditions(conditions);
                    }
                });
    }

    @Override public Condition getConditionById(int id) {
        return mLocalDataSource.getConditionById(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Packaging
    ///////////////////////////////////////////////////////////////////////////

    @Override public void savePackagings(@NonNull List<Packaging> packagings) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<Packaging>> getPackagings() {
        Observable<List<Packaging>> localPackagings = mLocalDataSource.getPackagings();
        Observable<List<Packaging>> remotePackagings = updatePackagings();
        return Observable.concat(localPackagings, remotePackagings).first();
    }

    @Override public Observable<List<Packaging>> updatePackagings() {
        return mRemoteDataSource.updatePackagings()
                .doOnNext(new Action1<List<Packaging>>() {
                    @Override public void call(List<Packaging> packagings) {
                        mLocalDataSource.savePackagings(packagings);
                    }
                });
    }

    @Override public Packaging getPackagingById(int id) {
        return mLocalDataSource.getPackagingById(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for OfferStatus
    ///////////////////////////////////////////////////////////////////////////

    @Override public void saveOfferStatuses(@NonNull List<OfferStatus> statuses) {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<List<OfferStatus>> updateOfferStatuses() {
        return mRemoteDataSource.updateOfferStatuses()
                .doOnNext(new Action1<List<OfferStatus>>() {
                    @Override public void call(List<OfferStatus> statuses) {
                        mLocalDataSource.saveOfferStatuses(statuses);
                    }
                });
    }

    @Override public Observable<List<OfferStatus>> getOfferStatuses() {
        Observable<List<OfferStatus>> localStatuses = mLocalDataSource.getOfferStatuses();
        Observable<List<OfferStatus>> remoteStatuses = updateOfferStatuses();
        return Observable.concat(localStatuses, remoteStatuses).first();
    }

    @Override public OfferStatus getOfferStatusById(int id) {
        return mLocalDataSource.getOfferStatusById(id);
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

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Advert
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Advert> saveAdvert(@NonNull Advert advert) {
        return mRemoteDataSource.saveAdvert(advert)
                .flatMap(new Func1<Advert, Observable<Advert>>() {
                    @Override public Observable<Advert> call(Advert advert) {
                        return mLocalDataSource.saveAdvert(advert);
                    }
                });
    }

    @Override public Observable<List<Advert>> getAdvertsPerFilter(@NonNull AdvertFilter filter) {
        return mRemoteDataSource.getAdvertsPerFilter(filter);
    }

    @Override public Observable<ResultList<Advert>> getAdvertResultListPerFilter(@NonNull AdvertFilter filter) {
        return mRemoteDataSource.getAdvertResultListPerFilter(filter);
    }

    @Override public Observable<ResultList<Advert>> getAdvertResultListPerPage(@NonNull String page) {
        return mRemoteDataSource.getAdvertResultListPerPage(page);
    }

    @Override public Observable<AdvertSubscriber> addRemoveAdvertWatching(@NonNull AdvertSubscriber subscriber) {
        return mRemoteDataSource.addRemoveAdvertWatching(subscriber);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Offer
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Offer> saveOffer(@NonNull Offer offer) {
        return mRemoteDataSource.saveOffer(offer)
                .flatMap(new Func1<Offer, Observable<Offer>>() {
                    @Override public Observable<Offer> call(Offer offer) {
                        return mLocalDataSource.saveOffer(offer);
                    }
                });
    }

    @Override public Observable<Offer> updateOffer(@NonNull Offer offer) {
        return mRemoteDataSource.updateOffer(offer);
    }

    @Override public Observable<List<Offer>> getOffersPerFilter(@NonNull OfferFilter filter) {
        return mRemoteDataSource.getOffersPerFilter(filter);
    }

    @Override public Observable<ResultList<Offer>> getOfferResultListPerFilter(@NonNull OfferFilter filter) {
        return mRemoteDataSource.getOfferResultListPerFilter(filter);
    }

    @Override public Observable<ResultList<Offer>> getOfferResultListPerPage(@NonNull String page) {
        return mRemoteDataSource.getOfferResultListPerPage(page);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Question
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Question> saveQuestion(@NonNull Question question) {
        return mRemoteDataSource.saveQuestion(question);
    }

    @Override public Observable<ResultList<Question>> getQuestionResultListPerFilter(@NonNull QuestionFilter filter) {
        return mRemoteDataSource.getQuestionResultListPerFilter(filter);
    }

    @Override public Observable<ResultList<Question>> getQuestionResultListPerPage(@NonNull String page) {
        return mRemoteDataSource.getQuestionResultListPerPage(page);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Answer
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<Answer> saveAnswer(@NonNull Answer answer) {
        return mRemoteDataSource.saveAnswer(answer);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods for User
    ///////////////////////////////////////////////////////////////////////////

    @Override public Observable<User> saveUser(@NonNull User user) {
        return null;
    }

    @Override public Observable<User> updateUser(@NonNull User user) {
        return mRemoteDataSource.updateUser(user)
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override public Observable<User> call(User user) {
                        return mLocalDataSource.updateUser(user);
                    }
                });
    }

    @Override public Observable<List<User>> getUsersPerFilter(@NonNull UserFilter filter) {
        Observable<List<User>> localUsers = mLocalDataSource.getUsersPerFilter(filter);
        Observable<List<User>> remoteUsers = mRemoteDataSource.getUsersPerFilter(filter);
        return Observable.concat(localUsers, remoteUsers).first();
    }

    @Override public Observable<ResultList<User>> getUserResultListPerFilter(@NonNull UserFilter filter) {
        return mRemoteDataSource.getUserResultListPerFilter(filter);
    }
}
