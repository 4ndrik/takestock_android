package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Subcategory;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class SubcategoryJson implements JsonModel {

    public int pk;
    public String name;

    public Subcategory getSubcategory() {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(pk);
        subcategory.setName(name);
        return subcategory;
    }
}
