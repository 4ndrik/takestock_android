package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.models.Size;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class SizeJsonMapper implements JsonMapper<List<Size>> {

    private static final String TYPES = "types";

    @Override public List<Size> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(TYPES);
        int length = jsonArray.length();
        List<Size> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            JSONObject typeJson = jsonArray.getJSONObject(i);
            String type = typeJson.getString(Integer.toString(i + 1));
            Size size = new Size();
            size.setType(type);
            result.add(size);
        }
        return result;
    }

    @Override public String toJsonString(List<Size> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
