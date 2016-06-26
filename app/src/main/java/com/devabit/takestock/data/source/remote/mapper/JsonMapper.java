package com.devabit.takestock.data.source.remote.mapper;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public interface JsonMapper<T> {
    T fromJsonString(String json) throws Exception;
    String toJsonString(T target) throws Exception;
}
