package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Size;
import com.devabit.takestock.data.source.local.entity.SizeEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class SizeEntityDataMapper {

    public List<SizeEntity> transformToEntityList(List<Size> sizes) {
        List<SizeEntity> result = new ArrayList<>(sizes.size());
        for (Size size : sizes) {
            SizeEntity entity = transformToEntity(size);
            result.add(entity);
        }
        return result;
    }

    public SizeEntity transformToEntity(Size size) {
        SizeEntity entity = new SizeEntity();
        entity.setType(size.getType());
        return entity;
    }

    public List<Size> transformFromEntityList(RealmResults<SizeEntity> entities) {
        List<Size> result = new ArrayList<>(entities.size());
        for (SizeEntity entity : entities) {
            Size size = transformFromEntity(entity);
            result.add(size);
        }
        return result;
    }

    public Size transformFromEntity(SizeEntity entity) {
        Size size = new Size();
        size.setType(entity.getType());
        return size;
    }
}
