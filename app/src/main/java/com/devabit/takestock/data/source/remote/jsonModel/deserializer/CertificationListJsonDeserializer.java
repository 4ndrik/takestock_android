package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.source.remote.jsonModel.CertificationListJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class CertificationListJsonDeserializer implements JsonDeserializer<CertificationListJson> {

    @Override public CertificationListJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        List<Certification> certifications = new ArrayList<>(jsonArray.size());
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int id = jsonObject.get("pk").getAsInt();
            String name = jsonObject.get("name").getAsString();
            JsonElement descriptionElement = jsonObject.get("description");
            String description = descriptionElement.isJsonNull() ? "" : descriptionElement.getAsString();
            JsonElement logoElement = jsonObject.get("logo");
            String logo = logoElement.isJsonNull() ? "" : logoElement.getAsString();

            Certification.Builder builder = new Certification.Builder()
                    .setId(id)
                    .setName(name)
                    .setDescription(description)
                    .setLogoUrl(logo);

            certifications.add(builder.build());
        }
        return new CertificationListJson(certifications);
    }
}
