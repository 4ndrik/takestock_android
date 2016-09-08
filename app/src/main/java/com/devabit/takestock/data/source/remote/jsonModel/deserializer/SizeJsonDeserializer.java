package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Size;
import com.devabit.takestock.data.source.remote.jsonModel.SizeJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class SizeJsonDeserializer implements JsonDeserializer<SizeJson> {

    @Override public SizeJson deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("types").getAsJsonArray();
        List<Size> sizes = new ArrayList<>(jsonArray.size());
        for (int id = 1; id <= jsonArray.size(); id++) {
            JsonObject object = jsonArray.get(id).getAsJsonObject();
            String type = object.get(String.valueOf(id)).getAsString();
            Size size = new Size(id, type);
            sizes.add(size);
        }
        return new SizeJson(sizes);
    }
}