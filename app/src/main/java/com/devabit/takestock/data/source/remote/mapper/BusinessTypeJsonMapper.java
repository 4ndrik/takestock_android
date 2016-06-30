package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.BusinessSubtype;
import com.devabit.takestock.data.model.BusinessType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 22/06/2016.
 */
public class BusinessTypeJsonMapper implements JsonMapper<BusinessType> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SUBTYPES = "sub";
    private final BusinessSubtypeJsonMapper mSubtypeMapper;

    public BusinessTypeJsonMapper() {
        mSubtypeMapper = new BusinessSubtypeJsonMapper();
    }

    public List<BusinessType> fromJsonStringToList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<BusinessType> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            String jsonString = jsonArray.getString(index);
            BusinessType type = fromJsonString(jsonString);
            result.add(type);
        }
        return result;
    }

    @Override public BusinessType fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        BusinessType type = new BusinessType();
        type.setId(jsonObject.getInt(ID));
        type.setName(jsonObject.getString(NAME));
        JSONArray jsonArray = jsonObject.getJSONArray(SUBTYPES);
        type.setSubtypes(subtypesFromJsonArray(jsonArray));
        return type;
    }

    private List<BusinessSubtype> subtypesFromJsonArray(JSONArray jsonArray) throws JSONException {
        List<BusinessSubtype> result = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            String json = jsonArray.getString(i);
            BusinessSubtype subtype = mSubtypeMapper.fromJsonString(json);
            result.add(subtype);
        }
        return result;
    }

    @Override public String toJsonString(BusinessType target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
