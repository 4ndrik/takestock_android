package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.ResultList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertResultListJsonMapper implements FromJsonMapper<ResultList<Advert>> {

    private static final String COUNT = "count";
    private static final String NEXT = "next";
    private static final String PREVIOUS = "previous";
    private static final String RESULTS = "results";

    @Override public ResultList<Advert> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        ResultList<Advert> resultList = new ResultList<>();
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
        List<Advert> adverts = new AdvertJsonMapper().fromJsonString(jsonObject.getString(RESULTS));
        resultList.setResults(adverts);
        return resultList;
    }
}
