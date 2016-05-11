package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Shipping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ShippingJsonMapper implements FromJsonMapper<List<Shipping>> {

    private static final String SHIPPING = "shipping";

    @Override public List<Shipping> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(SHIPPING);
        int length = jsonArray.length();
        List<Shipping> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            JSONObject typeJson = jsonArray.getJSONObject(i);
            int id = i + 1;
            String type = typeJson.getString(Integer.toString(id));
            Shipping shipping = new Shipping();
            shipping.setId(id);
            shipping.setType(type);
            result.add(shipping);
        }
        return result;
    }
}
