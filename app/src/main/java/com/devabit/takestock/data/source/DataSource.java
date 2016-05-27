package com.devabit.takestock.data.source;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.models.*;
import rx.Observable;

import java.util.List;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    Observable<AuthToken> obtainAuthTokenPerSignUp(@NonNull UserCredentials credentials);

    Observable<AuthToken> obtainAuthTokenPerSignIn(@NonNull UserCredentials credentials);

    void saveCategories(@NonNull List<Category> categories);

    Observable<List<Category>> updateCategories();

    Observable<List<Category>> getCategories();

    Category getCategoryById(int id);

    void saveSizes(@NonNull List<Size> sizes);

    Observable<List<Size>> updateSizes();

    Observable<List<Size>> getSizes();

    void saveCertifications(@NonNull List<Certification> certifications);

    Observable<List<Certification>> updateCertifications();

    Observable<List<Certification>> getCertifications();

    Certification getCertificationById(int id);

    void saveShippings(@NonNull List<Shipping> shippings);

    Observable<List<Shipping>> updateShippings();

    Observable<List<Shipping>> getShippings();

    Shipping getShippingById(int id);

    void saveConditions(@NonNull List<Condition> conditions);

    Observable<List<Condition>> updateConditions();

    Observable<List<Condition>> getConditions();

    Condition getConditionById(int id);

    void savePackagings(@NonNull List<Packaging> packagings);

    Observable<List<Packaging>> updatePackagings();

    Observable<List<Packaging>> getPackagings();

    Packaging getPackagingById(int id);

    void saveOfferStatuses(@NonNull List<OfferStatus> statuses);

    Observable<List<OfferStatus>> updateOfferStatuses();

    Observable<List<OfferStatus>> getOfferStatuses();

    OfferStatus getOfferStatusById(int id);

    Observable<Advert> saveAdvert(@NonNull Advert advert);

    Observable<ResultList<Advert>> getAdvertResultList();

    Observable<ResultList<Advert>> getAdvertResultListPerPage(@NonNull String page);

    Observable<ResultList<Advert>> getAdvertResultListPerFilter(@NonNull AdvertFilter filter);

}
