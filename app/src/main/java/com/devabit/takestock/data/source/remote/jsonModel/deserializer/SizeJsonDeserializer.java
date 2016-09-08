package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Size;
import com.devabit.takestock.data.source.remote.jsonModel.SizeListJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class SizeJsonDeserializer implements JsonDeserializer<SizeListJson> {

    @Override public SizeListJson deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("types").getAsJsonArray();
        List<Size> sizes = new ArrayList<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            int id = i + 1;
            String type = object.get(String.valueOf(id)).getAsString();
            Size size = new Size(id, type);
            sizes.add(size);
        }
        return new SizeListJson(sizes);
    }
}