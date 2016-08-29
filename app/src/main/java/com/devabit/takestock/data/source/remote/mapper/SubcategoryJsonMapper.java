package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Subcategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/08/2016.
 */
public class SubcategoryJsonMapper implements JsonMapper<List<Subcategory>> {

    private static final String ID = "pk";
    private static final String NAME = "name";

    @Override public List<Subcategory> fromJsonString(String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        List<Subcategory> result = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            result.add(fromJsonObject(jsonObject));
        }
        return result;
    }

    private Subcategory fromJsonObject(JSONObject jsonObject) throws JSONException {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(jsonObject.getInt(ID));
        subcategory.setName(jsonObject.getString(NAME));
        return subcategory;
    }

    @Override public String toJsonString(List<Subcategory> target) throws Exception {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
