package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.models.ResultList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public abstract class ResultListJsonMapper<T> implements JsonMapper<ResultList<T>> {

    protected static final String COUNT = "count";
    protected static final String NEXT = "next";
    protected static final String PREVIOUS = "previous";
    protected static final String RESULTS = "results";

    protected ResultList<T> getResultList(JSONObject jsonObject) throws JSONException {
        ResultList<T> resultList = new ResultList<>();
        resultList.setCount(jsonObject.getInt(COUNT));
        if (jsonObject.isNull(NEXT)) {
            resultList.setNext(null);
        } else {
            resultList.setNext(jsonObject.getString(NEXT));
        }

        if (jsonObject.isNull(PREVIOUS)) {
            resultList.setPrevious(null);
        } else {
            resultList.setPrevious(jsonObject.getString(PREVIOUS));
        }
        return resultList;
    }

    @Override public String toJsonString(ResultList<T> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
