package com.devabit.takestock.data.source.remote.mappers;

import org.json.JSONException;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public interface ToJsonMapper<T> {

    String toJsonString(T target) throws JSONException;
}
