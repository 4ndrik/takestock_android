package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.BusinessSubtype;
import com.devabit.takestock.data.models.BusinessType;
import com.devabit.takestock.data.source.local.entity.BusinessSubtypeEntity;
import com.devabit.takestock.data.source.local.entity.BusinessTypeEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 23/06/2016.
 */
public class BusinessTypeDataMapper {

    private final BusinessSubtypeDataMapper mSubtypeDataMapper;

    public BusinessTypeDataMapper() {
        mSubtypeDataMapper = new BusinessSubtypeDataMapper();
    }

    public List<BusinessType> transformFromEntityList(RealmResults<BusinessTypeEntity> entities) {
        List<BusinessType> result = new ArrayList<>(entities.size());
        for(BusinessTypeEntity entity : entities) {
            BusinessType type = transformFromEntity(entity);
            result.add(type);
        }
        return result;
    }

    public BusinessType transformFromEntity(BusinessTypeEntity entity) {
        BusinessType type = new BusinessType();
        type.setId(entity.getId());
        type.setName(entity.getName());
        List<BusinessSubtype> subtypes = mSubtypeDataMapper.transformFromEntityList(entity.getSubtypeEntities());
        type.setSubtypes(subtypes);
        return type;
    }

    public BusinessTypeEntity transformToEntity(BusinessType type) {
        BusinessTypeEntity entity = new BusinessTypeEntity();
        entity.setId(type.getId());
        entity.setName(type.getName());
        return entity;
    }

    public BusinessSubtypeEntity transformSubtypeToEntity(BusinessSubtype subtype) {
        return mSubtypeDataMapper.transformToEntity(subtype);
    }
}
