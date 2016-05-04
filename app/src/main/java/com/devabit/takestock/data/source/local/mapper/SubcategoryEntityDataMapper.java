package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.model.Subcategory;
import com.devabit.takestock.data.source.local.entity.SubcategoryEntity;
import io.realm.RealmList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class SubcategoryEntityDataMapper {

    public List<Subcategory> transformFromEntityList(RealmList<SubcategoryEntity> entities) {
        List<Subcategory> result = new ArrayList<>(entities.size());
        for(SubcategoryEntity entity : entities) {
            Subcategory subcategory = transformFromEntity(entity);
            result.add(subcategory);
        }
        return result;
    }

    public Subcategory transformFromEntity(SubcategoryEntity entity) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(entity.getId());
        subcategory.setName(entity.getName());
        return subcategory;
    }

    public SubcategoryEntity transformToEntity(Subcategory subcategory) {
        SubcategoryEntity entity = new SubcategoryEntity();
        entity.setId(subcategory.getId());
        entity.setName(subcategory.getName());
        return entity;
    }
}
