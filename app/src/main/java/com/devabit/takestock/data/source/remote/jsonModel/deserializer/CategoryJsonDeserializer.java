package com.devabit.takestock.data.source.remote.jsonModel.deserializer;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.data.source.remote.jsonModel.CategoryListJson;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class CategoryJsonDeserializer implements JsonDeserializer<CategoryListJson> {

    @Override public CategoryListJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        List<Category> categories = new ArrayList<>(jsonArray.size());
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            boolean isFood = jsonObject.get("is_food").getAsBoolean();
            List<Subcategory> subcategories = toSubcategoryList(jsonObject.getAsJsonArray("subcategories"));

            Category.Builder builder = new Category.Builder()
                    .setId(id)
                    .setName(name)
                    .setIsFood(isFood)
                    .setSubcategories(subcategories);

            categories.add(builder.build());
        }
        return new CategoryListJson(categories);
    }

    private List<Subcategory> toSubcategoryList(JsonArray jsonArray) {
        List<Subcategory> subcategories = new ArrayList<>(jsonArray.size());
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int id = jsonObject.get("pk").getAsInt();
            String name = jsonObject.get("name").getAsString();
            Subcategory subcategory = new Subcategory(id, name);
            subcategories.add(subcategory);
        }
        return subcategories;
    }
}
