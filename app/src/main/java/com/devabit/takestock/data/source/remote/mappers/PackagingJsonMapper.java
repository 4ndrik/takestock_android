package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Packaging;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PackagingJsonMapper implements JsonMapper<List<Packaging>> {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Override public List<Packaging> fromJsonString(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Packaging> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            Packaging packaging = new Packaging();
            packaging.setId(jsonObject.getInt(ID));
            packaging.setType(jsonObject.getString(NAME));
            result.add(packaging);
        }
        return result;
    }

    @Override public String toJsonString(List<Packaging> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }

}
