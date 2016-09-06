package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.local.entity.OfferEntity;
import com.devabit.takestock.data.source.local.entity.UserEntity;

/**
 * Created by Victor Artemyev on 27/05/2016.
 */
public class OfferEntityDataMapper {

    private final UserEntityDataMapper mUserMapper;

    public OfferEntityDataMapper() {
        mUserMapper = new UserEntityDataMapper();
    }

    public Offer transformFromEntity(OfferEntity entity) {
        Offer offer = new Offer();
        offer.setId(entity.getId());
        offer.setAdvertId(entity.getAdvertId());
        offer.setCounterOfferId(entity.getCounterOfferId());
        offer.setPrice(entity.getPrice());
        offer.setQuantity(entity.getQuantity());
        offer.setUserId(entity.getUserId());
        offer.setOfferStatusId(entity.getOfferStatusId());
        offer.setComment(entity.getComment());
        offer.setDateCreated(entity.getDateCreated());
        offer.setDateUpdated(entity.getUpdatedDate());
        User user = mUserMapper.transformFromEntity(entity.getUser());
        offer.setUser(user);
        return offer;
    }

    public OfferEntity transformToEntity(Offer offer) {
        OfferEntity entity = new OfferEntity();
        entity.setId(offer.getId());
        entity.setAdvertId(offer.getAdvertId());
        entity.setCounterOfferId(offer.getCounterOfferId());
        entity.setPrice(offer.getPrice());
        entity.setQuantity(offer.getQuantity());
        entity.setUserId(offer.getUserId());
        entity.setOfferStatusId(offer.getOfferStatusId());
        entity.setComment(offer.getComment());
        entity.setDateCreated(offer.getDateCreated());
        entity.setUpdatedDate(offer.getDateUpdated());
        UserEntity userEntity = mUserMapper.transformToEntity(offer.getUser());
        entity.setUser(userEntity);
        return entity;
    }
}
