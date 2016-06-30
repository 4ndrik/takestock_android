package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.model.BusinessSubtype;
import com.devabit.takestock.data.source.local.entity.BusinessSubtypeEntity;
import io.realm.RealmList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 23/06/2016.
 */
public class BusinessSubtypeDataMapper {

    public List<BusinessSubtype> transformFromEntityList(RealmList<BusinessSubtypeEntity> entities) {
        List<BusinessSubtype> result = new ArrayList<>(entities.size());
        for(BusinessSubtypeEntity entity : entities) {
            BusinessSubtype subtype = transformFromEntity(entity);
            result.add(subtype);
        }
        return result;
    }

    public BusinessSubtype transformFromEntity(BusinessSubtypeEntity entity) {
        BusinessSubtype subtype = new BusinessSubtype();
        subtype.setId(entity.getId());
        subtype.setName(entity.getName());
        return subtype;
    }

    public BusinessSubtypeEntity transformToEntity(BusinessSubtype subtype) {
        BusinessSubtypeEntity entity = new BusinessSubtypeEntity();
        entity.setId(subtype.getId());
        entity.setName(subtype.getName());
        return entity;
    }
}
