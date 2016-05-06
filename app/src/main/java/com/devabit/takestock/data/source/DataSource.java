package com.devabit.takestock.data.source;

import com.devabit.takestock.data.model.*;
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

    Observable<String> getAdverts();

    void saveSizes(List<Size> sizes);

    Observable<List<Size>> getSizes();

    void saveCertifications(List<Certification> certifications);

    Observable<List<Certification>> getCertifications();

    void saveShippings(List<Shipping> shippings);

    Observable<List<Shipping>> getShippings();

    void saveConditions(List<Condition> conditions);

    Observable<List<Condition>> getConditions();

}
