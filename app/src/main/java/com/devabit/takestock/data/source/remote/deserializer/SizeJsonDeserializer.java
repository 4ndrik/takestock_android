package com.devabit.takestock.data.source.remote.deserializer;

import com.devabit.takestock.data.source.remote.jsonModel.SizeJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class SizeJsonDeserializer implements JsonDeserializer<List<SizeJson>> {

    @Override public List<SizeJson> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("types").getAsJsonArray();
        List<SizeJson> jsons = new ArrayList<>(jsonArray.size());
        for (int i = 1; i <= jsonArray.size(); i++) {
            SizeJson json = new SizeJson();
            json.id = 1;
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            json.type = object.get(String.valueOf(i)).getAsString();
            jsons.add(json);
        }
        return jsons;
    }
}