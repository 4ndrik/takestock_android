package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.data.source.local.entity.CategoryEntity;
import com.devabit.takestock.data.source.local.entity.SubcategoryEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class CategoryEntityDataMapper {

    private final SubcategoryEntityDataMapper mSubcategoryMapper;

    public CategoryEntityDataMapper() {
        mSubcategoryMapper = new SubcategoryEntityDataMapper();
    }

    public List<Category> transformFromEntityList(RealmResults<CategoryEntity> categories) {
        List<Category> result = new ArrayList<>(categories.size());
        for(CategoryEntity entity : categories) {
            Category category = transformFromEntity(entity);
            result.add(category);
        }
        return result;
    }

    public Category transformFromEntity(CategoryEntity entity) {
        Category category = new Category();
        category.setId(entity.getId());
        category.setName(entity.getName());
        List<Subcategory> subcategories = mSubcategoryMapper.transformFromEntityList(entity.getSubcategories());
        category.setSubcategories(subcategories);
        return category;
    }

    public CategoryEntity transformToEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        return entity;
    }

    public SubcategoryEntity transformSubcategoryToEntity(Subcategory subcategory) {
        return mSubcategoryMapper.transformToEntity(subcategory);
    }
 }
