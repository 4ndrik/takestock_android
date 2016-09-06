package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.PaginatedList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public abstract class ResultListJsonMapper<T> implements JsonMapper<PaginatedList<T>> {

    protected static final String COUNT = "count";
    protected static final String NEXT = "next";
    protected static final String PREVIOUS = "previous";
    protected static final String RESULTS = "results";

    protected PaginatedList<T> getResultList(JSONObject jsonObject) throws JSONException {
        PaginatedList<T> paginatedList = new PaginatedList<>();
        paginatedList.setCount(jsonObject.getInt(COUNT));
        if (jsonObject.isNull(NEXT)) {
            paginatedList.setNext(null);
        } else {
            paginatedList.setNext(jsonObject.getString(NEXT));
        }

        if (jsonObject.isNull(PREVIOUS)) {
            paginatedList.setPrevious(null);
        } else {
            paginatedList.setPrevious(jsonObject.getString(PREVIOUS));
        }
        return paginatedList;
    }

    @Override public String toJsonString(PaginatedList<T> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
