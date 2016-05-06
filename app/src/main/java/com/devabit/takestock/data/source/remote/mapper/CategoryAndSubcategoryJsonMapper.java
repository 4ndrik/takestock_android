package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 02/05/2016.
 */
public class CategoryAndSubcategoryJsonMapper implements FromJsonMapper<List<Category>> {

    private static final String ID = "pk";
    private static final String NAME = "name";
    private static final String SUBCATEGORIES = "subcategories";

    @Override public List<Category> fromJsonString(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Category> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            Category category = new Category();
            category.setId(jsonObject.getInt(ID));
            category.setName(jsonObject.getString(NAME));
            List<Subcategory> subcategories = fromJsonArray(jsonObject.getJSONArray(SUBCATEGORIES));
            category.setSubcategories(subcategories);
            result.add(category);
        }
        return result;
    }

    private  List<Subcategory> fromJsonArray(JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        List<Subcategory> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            Subcategory subcategory = fromJsonObject(jsonObject);
            result.add(subcategory);
        }
        return result;
    }

    private static Subcategory fromJsonObject(JSONObject jsonObject) throws JSONException {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(jsonObject.getInt(ID));
        subcategory.setName(jsonObject.getString(NAME));
        return subcategory;
    }
}