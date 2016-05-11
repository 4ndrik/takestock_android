package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.source.remote.entity.ResultListEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class ResultListEntityJsonMapper implements FromJsonMapper<ResultListEntity> {

    private static final String COUNT = "count";
    private static final String NEXT = "next";
    private static final String PREVIOUS = "previous";
    private static final String RESULTS = "results";

    @Override public ResultListEntity fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        ResultListEntity response = new ResultListEntity();
        response.setCount(jsonObject.getInt(COUNT));
        response.setNext(jsonObject.getString(NEXT));
        response.setPrevious(jsonObject.getString(PREVIOUS));
        response.setResults(jsonObject.getString(RESULTS));
        return response;
    }
}
