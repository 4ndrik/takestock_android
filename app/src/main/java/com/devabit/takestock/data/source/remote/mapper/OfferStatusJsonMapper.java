package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.OfferStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferStatusJsonMapper implements JsonMapper<List<OfferStatus>> {

    private static final String STATUS = "status";

    @Override public List<OfferStatus> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(STATUS);
        int length = jsonArray.length();
        List<OfferStatus> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            JSONObject typeJson = jsonArray.getJSONObject(i);
            int id = i + 1;
            String type = typeJson.getString(Integer.toString(id));
            OfferStatus status = new OfferStatus();
            status.setId(id);
            status.setType(type);
            result.add(status);
        }
        return result;
    }

    @Override public String toJsonString(List<OfferStatus> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
