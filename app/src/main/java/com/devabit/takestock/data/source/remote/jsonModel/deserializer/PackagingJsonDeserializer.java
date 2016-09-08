package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Packaging;
import com.devabit.takestock.data.source.remote.jsonModel.PackagingJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class PackagingJsonDeserializer implements JsonDeserializer<PackagingJson> {
    @Override public PackagingJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        List<Packaging> packagings = new ArrayList<>(jsonArray.size());
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            Packaging packaging = new Packaging(id, name);
            packagings.add(packaging);
        }
        return new PackagingJson(packagings);
    }
}
