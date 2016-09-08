package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Condition;
import com.devabit.takestock.data.source.remote.jsonModel.ConditionListJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class ConditionJsonDeserializer implements JsonDeserializer<ConditionListJson> {
    @Override public ConditionListJson deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("conditions").getAsJsonArray();
        List<Condition> conditions = new ArrayList<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            int id = i + 1;
            String state = object.get(String.valueOf(id)).getAsString();
            Condition condition = new Condition(id, state);
            conditions.add(condition);
        }
        return new ConditionListJson(conditions);
    }
}
