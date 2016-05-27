package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.ResultList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertResultListJsonMapper extends ResultListJsonMapper<Advert> {

    @Override public ResultList<Advert> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        ResultList<Advert> resultList = getResultList(jsonObject);
        List<Advert> adverts = new AdvertJsonMapper()
                .fromJsonStringToList(jsonObject.getString(RESULTS));
        resultList.setResults(adverts);
        return resultList;
    }
}
