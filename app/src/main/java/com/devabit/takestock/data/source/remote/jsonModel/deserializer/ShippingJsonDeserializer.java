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
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            int id = i + 1;
            String type = object.get(String.valueOf(id)).getAsString();
            Shipping shipping = new Shipping(id, type);
            shippings.add(shipping);
        }
        return new ShippingListJson(shippings);
    }
}
