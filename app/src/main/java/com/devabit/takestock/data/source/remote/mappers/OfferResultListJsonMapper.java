package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.ResultList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public class OfferResultListJsonMapper extends ResultListJsonMapper<Offer> {

    @Override public ResultList<Offer> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        ResultList<Offer> offerList = getResultList(jsonObject);
        List<Offer> result = new OfferJsonMapper().fromJsonStringToList(jsonObject.getString(RESULTS));
        offerList.setResults(result);
        return offerList;
    }
}
