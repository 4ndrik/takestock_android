package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Shipping;
import com.devabit.takestock.data.source.remote.jsonModel.ShippingListJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class ShippingJsonDeserializer implements JsonDeserializer<ShippingListJson> {
    @Override public ShippingListJson deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("shipping").getAsJsonArray();
        List<Shipping> shippings = new ArrayList<>(jsonArray.size());
        for (int id = 1; id <= jsonArray.size(); id++) {
            JsonObject object = jsonArray.get(id).getAsJsonObject();
            String type = object.get(String.valueOf(id)).getAsString();
            Shipping shipping = new Shipping(id, type);
            shippings.add(shipping);
        }
        return new ShippingListJson(shippings);
    }
}
