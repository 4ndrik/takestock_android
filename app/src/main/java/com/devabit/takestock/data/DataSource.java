package com.devabit.takestock.data;

import com.devabit.takestock.data.model.AccessToken;
import com.devabit.takestock.data.model.UserCredentials;
import rx.Observable;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    Observable<AccessToken> obtainAccessToken(UserCredentials credentials);

    Observable<String> getCategories();

    Observable<String> getAdverts();

    Observable<String> getSizeTypes();

    Observable<String> getCertifications();

    Observable<String> getShipping();

    Observable<String> getConditions();
}
