package com.devabit.takestock.data.source;

import com.devabit.takestock.data.model.*;
import rx.Observable;

import java.util.List;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    Observable<AuthToken> obtainAuthToken(UserCredentials credentials);

    Observable<String> getCategories();

    Observable<String> getAdverts();

    void saveSizes(List<Size> sizeList);

    Observable<List<Size>> getSizes();

    void saveCertifications(List<Certification> certificationList);

    Observable<List<Certification>> getCertifications();

    void saveShipping(List<Shipping> shippingList);

    Observable<List<Shipping>> getShipping();

    void saveConditions(List<Condition> conditionList);

    Observable<List<Condition>> getConditions();

}
