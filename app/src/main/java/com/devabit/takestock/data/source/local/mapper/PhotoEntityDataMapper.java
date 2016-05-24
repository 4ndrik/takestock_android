package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.data.source.local.entity.PhotoEntity;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class PhotoEntityDataMapper {

    public Photo transformFromEntity(PhotoEntity entity) {
        Photo photo = new Photo();
        photo.setId(entity.getId());
        photo.setImagePath(entity.getImagePath());
        photo.setWidth(entity.getWidth());
        photo.setHeight(entity.getHeight());
        photo.setMain(entity.isMain());
        return photo;
    }

    public PhotoEntity transformToEntity(Photo photo) {
        PhotoEntity entity = new PhotoEntity();
        entity.setId(photo.getId());
        entity.setImagePath(photo.getImagePath());
        entity.setWidth(photo.getWidth());
        entity.setHeight(photo.getHeight());
        entity.setMain(photo.isMain());
        return entity;
    }
}
