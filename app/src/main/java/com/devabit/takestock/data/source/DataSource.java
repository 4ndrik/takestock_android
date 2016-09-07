package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.filter.UserFilter;
import com.devabit.takestock.data.model.*;
import rx.Observable;

import java.util.List;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    /********* Entries Methods  ********/

    Observable<AuthToken> signUp(@NonNull UserCredentials credentials);

    Observable<AuthToken> signIn(@NonNull UserCredentials credentials);

    /********* Categories Methods  ********/

    Observable<List<Category>> saveCategories(@NonNull List<Category> categories);

    Observable<List<Category>> refreshCategories();

    Observable<List<Category>> getCategories();

    Category getCategoryWithId(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Size
    ///////////////////////////////////////////////////////////////////////////

    void saveSizes(@NonNull List<Size> sizes);

    Observable<List<Size>> updateSizes();

    Observable<List<Size>> getSizes();

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Certification
    ///////////////////////////////////////////////////////////////////////////

    void saveCertifications(@NonNull List<Certification> certifications);

    Observable<List<Certification>> updateCertifications();

    Observable<List<Certification>> getCertifications();

    Certification getCertificationById(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Shipping
    ///////////////////////////////////////////////////////////////////////////

    void saveShippings(@NonNull List<Shipping> shippings);

    Observable<List<Shipping>> updateShippings();

    Observable<List<Shipping>> getShippings();

    Shipping getShippingById(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Condition
    ///////////////////////////////////////////////////////////////////////////

    void saveConditions(@NonNull List<Condition> conditions);

    Observable<List<Condition>> updateConditions();

    Observable<List<Condition>> getConditions();

    Condition getConditionById(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Packaging
    ///////////////////////////////////////////////////////////////////////////

    void savePackagings(@NonNull List<Packaging> packagings);

    Observable<List<Packaging>> updatePackagings();

    Observable<List<Packaging>> getPackagings();

    Packaging getPackagingById(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for OfferStatus
    ///////////////////////////////////////////////////////////////////////////

    void saveOfferStatuses(@NonNull List<OfferStatus> statuses);

    Observable<List<OfferStatus>> updateOfferStatuses();

    Observable<List<OfferStatus>> getOfferStatuses();

    OfferStatus getOfferStatusById(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for BusinessType
    ///////////////////////////////////////////////////////////////////////////

    void saveBusinessTypes(@NonNull List<BusinessType> businessTypes);

    Observable<List<BusinessType>> updateBusinessTypes();

    Observable<List<BusinessType>> getBusinessTypes();

    BusinessType getBusinessTypeById(int id);

    BusinessSubtype getBusinessSubtypeById(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Advert
    ///////////////////////////////////////////////////////////////////////////

    Observable<Advert> saveAdvert(@NonNull Advert advert);

    Observable<List<Advert>> getAdvertsWithFilter(@NonNull AdvertFilter filter);

    Observable<PaginatedList<Advert>> getPaginatedAdvertListWithFilter(@NonNull AdvertFilter filter);

    Observable<PaginatedList<Advert>> getAdvertResultListPerPage(@NonNull String page);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for AdvertSubscriber
    ///////////////////////////////////////////////////////////////////////////

    Observable<AdvertSubscriber> addRemoveAdvertWatching(@NonNull AdvertSubscriber subscriber);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Offer
    ///////////////////////////////////////////////////////////////////////////

    Observable<Offer> saveOffer(@NonNull Offer offer);

    Observable<Offer> updateOffer(@NonNull Offer offer);

    Observable<List<Offer>> getOffersPerFilter(@NonNull OfferFilter filter);

    Observable<PaginatedList<Offer>> getOfferResultListPerFilter(@NonNull OfferFilter filter);

    Observable<PaginatedList<Offer>> getOfferResultListPerPage(@NonNull String page);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Question
    ///////////////////////////////////////////////////////////////////////////

    Observable<Question> saveQuestion(@NonNull Question question);

    Observable<PaginatedList<Question>> getQuestionResultListPerFilter(@NonNull QuestionFilter filter);

    Observable<PaginatedList<Question>> getQuestionResultListPerPage(@NonNull String page);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Answer
    ///////////////////////////////////////////////////////////////////////////

    Observable<Answer> saveAnswer(@NonNull Answer answer);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for User
    ///////////////////////////////////////////////////////////////////////////

    Observable<User> saveUser(@NonNull User user);

    Observable<User> updateUser(@NonNull User user);

    Observable<List<User>> getUsersPerFilter(@NonNull UserFilter filter);

    Observable<PaginatedList<User>> getUserResultListPerFilter(@NonNull UserFilter filter);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for Payment
    ///////////////////////////////////////////////////////////////////////////

    Observable<String> addPayment(@NonNull Payment payment);

}
