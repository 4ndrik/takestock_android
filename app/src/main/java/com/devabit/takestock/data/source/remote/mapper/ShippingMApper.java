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
public class ShippingMapper implements FromJsonMapper<List<Shipping>> {

    private static final String SHIPPING = "shipping";

    @Override public List<Shipping> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(SHIPPING);
        int length = jsonArray.length();
        List<Shipping> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            JSONObject typeJson = jsonArray.getJSONObject(i);
            String type = typeJson.getString(Integer.toString(i + 1));
            Shipping shipping = new Shipping();
            shipping.setType(type);
            result.add(shipping);
        }
        return result;
    }
}
