package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class CategoryJson implements JsonModel {

    public int id;
    public String name;
    public List<SubcategoryJson> subcategories;

    public Category getCategory() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setSubcategories(getSubcategoryList());
        return category;
    }

    private List<Subcategory> getSubcategoryList() {
        List<Subcategory> list = new ArrayList<>(subcategories.size());
        for (SubcategoryJson json : subcategories) {
            list.add(json.getSubcategory());
        }
        return list;
    }
}
