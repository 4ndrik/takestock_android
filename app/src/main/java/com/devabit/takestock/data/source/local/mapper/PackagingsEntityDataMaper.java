package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.model.Packaging;
import com.devabit.takestock.data.source.local.entity.PackagingEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PackagingsEntityDataMaper {

    public List<PackagingEntity> transformToEntityList(List<Packaging> packagings) {
        List<PackagingEntity> result = new ArrayList<>(packagings.size());
        for (Packaging packaging : packagings) {
            PackagingEntity entity = transformToEntity(packaging);
            result.add(entity);
        }
        return result;
    }

    public PackagingEntity transformToEntity(Packaging packaging) {
        PackagingEntity entity = new PackagingEntity();
        entity.setId(packaging.getId());
        entity.setType(packaging.getType());
        return entity;
    }

    public List<Packaging> transformFromEntityList(RealmResults<PackagingEntity> entities) {
        List<Packaging> result = new ArrayList<>(entities.size());
        for (PackagingEntity entity : entities) {
            Packaging packaging = transformFromEntity(entity);
            result.add(packaging);
        }
        return result;
    }

    public Packaging transformFromEntity(PackagingEntity entity) {
        Packaging packaging = new Packaging();
        packaging.setType(entity.getType());
        return packaging;
    }
}
