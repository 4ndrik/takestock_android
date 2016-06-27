package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.models.Condition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ConditionJsonMapper implements JsonMapper<List<Condition>> {

    private static final String CONDITIONS = "conditions";

    @Override public List<Condition> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(CONDITIONS);
        int length = jsonArray.length();
        List<Condition> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            JSONObject typeJson = jsonArray.getJSONObject(i);
            int id = i + 1;
            String state = typeJson.getString(Integer.toString(id));
            Condition condition = new Condition();
            condition.setId(id);
            condition.setState(state);
            result.add(condition);
        }
        return result;
    }

    @Override public String toJsonString(List<Condition> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}