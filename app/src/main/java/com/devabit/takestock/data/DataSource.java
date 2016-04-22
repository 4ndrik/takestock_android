package com.devabit.takestock.data;

import android.support.annotation.NonNull;
import rx.Observable;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface DataSource {

    Observable<String> obtainTokenAuth(@NonNull String userName, @NonNull String password);
    Observable<String> verifyToken(@NonNull String token);

    Observable<String> getCategories();
    Observable<String> getAdverts();
    Observable<String> getSizeTypes();
    Observable<String> getCertifications();
    Observable<String> getShipping();
    Observable<String> getConditions();
}
