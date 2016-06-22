package com.devabit.takestock.data.source.remote.mapper;

import org.json.JSONException;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public interface JsonMapper<T> {
    T fromJsonString(String json) throws JSONException;
    String toJsonString(T target) throws JSONException;
}
