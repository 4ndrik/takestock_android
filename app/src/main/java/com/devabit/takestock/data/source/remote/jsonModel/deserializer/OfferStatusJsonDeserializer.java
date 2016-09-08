package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.OfferStatus;
import com.devabit.takestock.data.source.remote.jsonModel.OfferStatusListJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class OfferStatusJsonDeserializer implements JsonDeserializer<OfferStatusListJson> {

    @Override public OfferStatusListJson deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("status").getAsJsonArray();
        List<OfferStatus> statuses = new ArrayList<>(jsonArray.size());
        for (int id = 1; id <= jsonArray.size(); id++) {
            JsonObject object = jsonArray.get(id).getAsJsonObject();
            String type = object.get(String.valueOf(id)).getAsString();
            OfferStatus offerStatus = new OfferStatus(id, type);
            statuses.add(offerStatus);
        }
        return new OfferStatusListJson(statuses);
    }
}
