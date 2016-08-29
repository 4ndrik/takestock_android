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
public class CategoryJsonMapper implements JsonMapper<List<Category>> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SUBCATEGORIES = "subcategories";

    private final SubcategoryJsonMapper mSubcategoryMapper;

    public CategoryJsonMapper() {
        mSubcategoryMapper = new SubcategoryJsonMapper();
    }

    @Override public List<Category> fromJsonString(String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Category> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            Category category = new Category();
            category.setId(jsonObject.getInt(ID));
            category.setName(jsonObject.getString(NAME));
            List<Subcategory> subcategories = mSubcategoryMapper.fromJsonString(jsonObject.getString(SUBCATEGORIES));
            category.setSubcategories(subcategories);
            result.add(category);
        }
        return result;
    }

    @Override public String toJsonString(List<Category> target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
