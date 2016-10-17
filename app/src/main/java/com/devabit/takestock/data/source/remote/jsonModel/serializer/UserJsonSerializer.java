package com.devabit.takestock.data.source.remote.jsonModel.serializer;

import com.devabit.takestock.data.source.remote.jsonModel.UserEditorJson;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Victor Artemyev on 14/10/2016.
 */

public class UserJsonSerializer implements JsonSerializer<UserEditorJson> {

    @Override public JsonElement serialize(UserEditorJson userJson, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jObj = (JsonObject) new GsonBuilder().create().toJsonTree(userJson);
        if (userJson.bussines_type <= 0) {
            jObj.remove("bussines_type");
        }
        if (userJson.business_sub_type <= 0) {
            jObj.remove("business_sub_type");
        }
        if (userJson.bussines_name == null || userJson.bussines_name.isEmpty()) {
            jObj.remove("bussines_name");
        }
        if (userJson.postcode == null || userJson.postcode.isEmpty()) {
            jObj.remove("postcode");
        }
        if (userJson.photo_b64 == null || userJson.photo_b64.isEmpty()) {
            jObj.remove("photo_b64");
        }
        return jObj;
    }
}
