package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.data.source.local.entity.AdvertEntity;
import com.devabit.takestock.data.source.local.entity.PhotoEntity;
import com.devabit.takestock.data.source.local.entity.StringEntity;
import com.devabit.takestock.data.source.local.entity.UserEntity;
import io.realm.RealmList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertEntityDataMapper {

    private final PhotoEntityDataMapper mPhotoMapper;
    private final UserEntityDataMapper mUserMapper;

    public AdvertEntityDataMapper() {
        mPhotoMapper = new PhotoEntityDataMapper();
        mUserMapper = new UserEntityDataMapper();
    }

    public List<Advert> transformFromEntitiesToList(List<AdvertEntity> entities) {
        List<Advert> result = new ArrayList<>(entities.size());
        for (AdvertEntity entity : entities) {
            Advert advert = transformFromEntity(entity);
            result.add(advert);
        }
        return result;
    }

    public Advert transformFromEntity(AdvertEntity entity) {
        Advert advert = new Advert();
        advert.setId(entity.getId());
        advert.setName(entity.getName());
        advert.setDateCreatedAt(entity.getDateCreatedAt());
        advert.setDateExpiresAt(entity.getDateExpiresAt());
        advert.setDateUpdatedAt(entity.getDateUpdatedAt());
        advert.setIntendedUse(entity.getIntendedUse());
        advert.setGuidePrice(entity.getGuidePrice());
        advert.setDescription(entity.getDescription());
        advert.setLocation(entity.getLocation());
        advert.setShippingId(entity.getShippingId());
        advert.setVatExempt(entity.isVatExempt());
        advert.setAuthorId(entity.getAuthorId());
        advert.setCategoryId(entity.getCategoryId());
        advert.setSubCategoryId(entity.getSubCategoryId());
        advert.setPackagingId(entity.getPackagingId());
        advert.setMinOrderQuantity(entity.getMinOrderQuantity());
        advert.setSize(entity.getSize());
        advert.setCertificationId(entity.getCertificationId());
        advert.setCertificationExtra(entity.getCertificationExtra());
        advert.setConditionId(entity.getConditionId());
        advert.setItemsCount(entity.getItemsCount());
        advert.setPackagingName(entity.getPackagingName());
        advert.setOffersCount(entity.getOffersCount());
        advert.setQuestionsCount(entity.getQuestionsCount());
        advert.setDaysLeft(entity.getDaysLeft());

        User user = mUserMapper.transformFromEntity(entity.getUser());
        advert.setUser(user);

        RealmList<StringEntity> tagEntities = entity.getTags();
        List<String> tags = new ArrayList<>(tagEntities.size());
        for (StringEntity tagEntity : tagEntities) {
            tags.add(tagEntity.getValue());
        }
        advert.setTags(tags);

        RealmList<PhotoEntity> photoEntities = entity.getPhotos();
        List<Photo> photos = new ArrayList<>(photoEntities.size());
        for (PhotoEntity photoEntity : photoEntities) {
            Photo photo = mPhotoMapper.transformFromEntity(photoEntity);
            photos.add(photo);
        }
        advert.setPhotos(photos);

        return advert;
    }

    public AdvertEntity transformToEntity(Advert advert) {
        AdvertEntity entity = new AdvertEntity();
        entity.setId(advert.getId());
        entity.setName(advert.getName());
        entity.setDateCreatedAt(advert.getDateCreatedAt());
        entity.setDateExpiresAt(advert.getDateExpiresAt());
        entity.setDateUpdatedAt(advert.getDateUpdatedAt());
        entity.setIntendedUse(advert.getIntendedUse());
        entity.setGuidePrice(advert.getGuidePrice());
        entity.setDescription(advert.getDescription());
        entity.setLocation(advert.getLocation());
        entity.setShippingId(advert.getShippingId());
        entity.setVatExempt(advert.isVatExempt());
        entity.setAuthorId(advert.getAuthorId());
        entity.setCategoryId(advert.getCategoryId());
        entity.setSubCategoryId(advert.getSubCategoryId());
        entity.setPackagingId(advert.getPackagingId());
        entity.setMinOrderQuantity(advert.getMinOrderQuantity());
        entity.setSize(advert.getSize());
        entity.setCertificationId(advert.getCertificationId());
        entity.setCertificationExtra(advert.getCertificationExtra());
        entity.setConditionId(advert.getConditionId());
        entity.setItemsCount(advert.getItemsCount());
        entity.setPackagingName(advert.getPackagingName());
        entity.setOffersCount(advert.getOffersCount());
        entity.setQuestionsCount(advert.getQuestionsCount());
        entity.setDaysLeft(advert.getDaysLeft());
        UserEntity userEntity = mUserMapper.transformToEntity(advert.getUser());
        entity.setUser(userEntity);
        return entity;
    }

    public StringEntity transformTagToEntity(String tag) {
        StringEntity entity = new StringEntity();
        entity.setValue(tag);
        return entity;
    }

    public PhotoEntity transformPhotoToEntity(Photo photo) {
        return mPhotoMapper.transformToEntity(photo);
    }
}
