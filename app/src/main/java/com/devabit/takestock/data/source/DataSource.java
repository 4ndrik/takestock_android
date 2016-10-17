package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.AdvertFilter;
import com.devabit.takestock.data.filter.OfferFilter;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.model.*;
import rx.Observable;

import java.util.List;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    /********* Entries Methods  *********/

    Observable<AuthToken> signUp(@NonNull UserCredentials credentials);

    Observable<AuthToken> signIn(@NonNull UserCredentials credentials);


    /********* Categories Methods  *********/

    Observable<List<Category>> saveCategories(@NonNull List<Category> categories);

    Observable<List<Category>> refreshCategories();

    Observable<List<Category>> getCategories();


    /********* Sizes Methods  *********/

    Observable<List<Size>> saveSizes(@NonNull List<Size> sizes);

    Observable<List<Size>> refreshSizes();

    Observable<List<Size>> getSizes();


    /********* Certifications Methods  *********/

    Observable<List<Certification>> saveCertifications(@NonNull List<Certification> certifications);

    Observable<List<Certification>> refreshCertifications();

    Observable<List<Certification>> getCertifications();

    Certification getCertificationWithId(int id);


    /********* Shippings Methods  *********/

    Observable<List<Shipping>> saveShippings(@NonNull List<Shipping> shippings);

    Observable<List<Shipping>> refreshShippings();

    Observable<List<Shipping>> getShippings();

    Shipping getShippingWithId(int id);


    /********* Conditions Methods  *********/

    Observable<List<Condition>> saveConditions(@NonNull List<Condition> conditions);

    Observable<List<Condition>> refreshConditions();

    Observable<List<Condition>> getConditions();

    Condition getConditionWithId(int id);


    /********* Packagings Methods  *********/

    Observable<List<Packaging>> savePackagings(@NonNull List<Packaging> packagings);

    Observable<List<Packaging>> refreshPackagings();

    Observable<List<Packaging>> getPackagings();

    Packaging getPackagingWithId(int id);


    /********* OfferStatuses Methods  *********/

    Observable<List<OfferStatus>> saveOfferStatuses(@NonNull List<OfferStatus> statuses);

    Observable<List<OfferStatus>> refreshOfferStatuses();

    Observable<List<OfferStatus>> getOfferStatuses();

    OfferStatus getOfferStatusWithId(int id);

    ///////////////////////////////////////////////////////////////////////////
    // Methods for BusinessType
    ///////////////////////////////////////////////////////////////////////////

    void saveBusinessTypes(@NonNull List<BusinessType> businessTypes);

    Observable<List<BusinessType>> updateBusinessTypes();

    Observable<List<BusinessType>> getBusinessTypes();

    BusinessType getBusinessTypeById(int id);

    BusinessSubtype getBusinessSubtypeById(int id);


    /********* Advert Methods  *********/

    Observable<Advert> saveAdvert(@NonNull Advert advert);

    Observable<Advert> editAdvert(@NonNull Advert advert);

    Observable<PaginatedList<Advert>> getPaginatedAdvertListWithFilter(@NonNull AdvertFilter filter);

    Observable<PaginatedList<Advert>> getPaginatedAdvertListPerPage(@NonNull String page);

    Observable<Advert.Subscriber> addRemoveAdvertWatching(int advertId);

    Observable<Advert> viewAdvertWithId(int advertId);

    Observable<Advert> unnotifyAdvertWithId(int advertId);


    /********* Offers Methods  *********/

    Observable<Offer> makeOffer(@NonNull Offer offer);

    Observable<Offer.Accept> acceptOffer(@NonNull Offer.Accept accept);

    Observable<Offer> getOfferWithId(int offerId);

    Observable<PaginatedList<Offer>> getPaginatedOfferListWithFilter(@NonNull OfferFilter filter);

    Observable<PaginatedList<Offer>> getPaginatedOfferListPerPage(@NonNull String page);


    /********* Questions Methods  *********/

    Observable<Question> saveQuestion(@NonNull Question question);

    Observable<PaginatedList<Question>> getPaginatedQuestionListWithFilter(@NonNull QuestionFilter filter);

    Observable<PaginatedList<Question>> getPaginatedQuestionListPerPage(@NonNull String page);


    /********** Answers Methods **********/

    Observable<Answer> saveAnswer(@NonNull Answer answer);


    /********** User Methods **********/

    Observable<User> saveUser(@NonNull User user);

    Observable<User> updateUser(@NonNull User user);

    Observable<User> refreshAccountUserWithId(int userId);

    Observable<User> getAccountUserWithId(int userId);

    Observable<User> getUserWithId(int id);

    Observable<Boolean> changePassword(String currentPass, String newPass);


    /********** Payment Methods **********/

    Observable<Payment> makePayment(@NonNull Payment payment);

}
