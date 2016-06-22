package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.models.BusinessSubtype;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 22/06/2016.
 */
public class BusinessSubtypeJsonMapper implements JsonMapper<BusinessSubtype> {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Override public BusinessSubtype fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        BusinessSubtype subtype = new BusinessSubtype();
        subtype.setId(jsonObject.getInt(ID));
        subtype.setName(jsonObject.getString(NAME));
        return subtype;
    }

    @Override public String toJsonString(BusinessSubtype target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
