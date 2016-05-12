package com.devabit.takestock.data.source;

import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.model.Advert;
import rx.Observable;

import java.util.List;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    Observable<AuthToken> obtainAuthTokenPerSignUp(UserCredentials credentials);

    Observable<AuthToken> obtainAuthTokenPerSignIn(UserCredentials credentials);

    void saveCategories(List<Category> categories);

    Observable<List<Category>> getCategories();

    Observable<List<Advert>> getAdverts();

    void saveSizes(List<Size> sizes);

    Observable<List<Size>> getSizes();

    void saveCertifications(List<Certification> certifications);

    Observable<List<Certification>> getCertifications();

    Certification getCertificationById(int id);

    void saveShippings(List<Shipping> shippings);

    Observable<List<Shipping>> getShippings();

    Shipping getShippingById(int id);

    void saveConditions(List<Condition> conditions);

    Observable<List<Condition>> getConditions();

    Condition getConditionById(int id);

    void savePackagings(List<Packaging> packagings);
    
    Observable<List<Packaging>> getPackagings();

}
