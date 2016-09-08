package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Category;

import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class CategoryJson implements JsonModel {

    private final List<Category> categories;

    public CategoryJson(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
