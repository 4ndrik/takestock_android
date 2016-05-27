package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.OfferStatus;
import com.devabit.takestock.data.source.local.entity.OfferStatusEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferStatusEntityDataMapper {

    public List<OfferStatusEntity> transformToEntityList(List<OfferStatus> statuses) {
        List<OfferStatusEntity> result = new ArrayList<>(statuses.size());
        for (OfferStatus offerStatus : statuses) {
            OfferStatusEntity entity = transformToEntity(offerStatus);
            result.add(entity);
        }
        return result;
    }

    public OfferStatusEntity transformToEntity(OfferStatus status) {
        OfferStatusEntity entity = new OfferStatusEntity();
        entity.setId(status.getId());
        entity.setType(status.getType());
        return entity;
    }

    public List<OfferStatus> transformFromEntityList(RealmResults<OfferStatusEntity> entities) {
        List<OfferStatus> result = new ArrayList<>(entities.size());
        for (OfferStatusEntity entity : entities) {
            OfferStatus offerStatus = transformFromEntity(entity);
            result.add(offerStatus);
        }
        return result;
    }

    public OfferStatus transformFromEntity(OfferStatusEntity entity) {
        if (entity == null) return null;
        OfferStatus status = new OfferStatus();
        status.setId(entity.getId());
        status.setType(entity.getType());
        return status;
    }
}
