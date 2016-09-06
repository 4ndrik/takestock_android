package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.PaginatedList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public class OfferPaginatedListJsonMapper extends PaginatedListJsonMapper<Offer> {

    @Override public PaginatedList<Offer> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        PaginatedList<Offer> offerList = getResultList(jsonObject);
        List<Offer> result = new OfferJsonMapper().fromJsonStringToList(jsonObject.getString(RESULTS));
        offerList.setResults(result);
        return offerList;
    }
}
