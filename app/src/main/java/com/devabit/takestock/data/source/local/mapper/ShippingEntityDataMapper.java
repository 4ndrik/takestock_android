package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Shipping;
import com.devabit.takestock.data.source.local.entity.ShippingEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class ShippingEntityDataMapper {

    public List<ShippingEntity> transformToEntityList(List<Shipping> shippings) {
        List<ShippingEntity> result = new ArrayList<>(shippings.size());
        for (Shipping shipping : shippings) {
            ShippingEntity entity = transformToEntity(shipping);
            result.add(entity);
        }
        return result;
    }

    public ShippingEntity transformToEntity(Shipping shipping) {
        ShippingEntity entity = new ShippingEntity();
        entity.setId(shipping.getId());
        entity.setType(shipping.getType());
        return entity;
    }

    public List<Shipping> transformFromEntityList(RealmResults<ShippingEntity> entities) {
        List<Shipping> result = new ArrayList<>(entities.size());
        for (ShippingEntity entity : entities) {
            Shipping shipping = transformFromEntity(entity);
            result.add(shipping);
        }
        return result;
    }

    public Shipping transformFromEntity(ShippingEntity entity) {
        if (entity == null) return null;
        Shipping shipping = new Shipping();
        shipping.setId(entity.getId());
        shipping.setType(entity.getType());
        return shipping;
    }
}
