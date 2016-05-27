package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.data.source.local.entity.PhotoEntity;
import com.devabit.takestock.data.source.local.entity.UserEntity;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class UserEntityDataMapper {

    private final PhotoEntityDataMapper mPhotoMapper;

    public UserEntityDataMapper() {
        mPhotoMapper = new PhotoEntityDataMapper();
    }

    public UserEntityDataMapper(PhotoEntityDataMapper photoMapper) {
        mPhotoMapper = photoMapper;
    }

    public UserEntity transformToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUserName(user.getUserName());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setDateJoined(user.getDateJoined());
        entity.setDateLastLogin(user.getDateLastLogin());
        entity.setSuperuser(user.isSuperuser());
        entity.setStaff(user.isStaff());
        entity.setActive(user.isActive());
        entity.setSubscribed(user.isSubscribed());
        entity.setVerified(user.isVerified());
        entity.setVatExempt(user.isVatExempt());
        entity.setAvgRating(user.getAvgRating());
        PhotoEntity photoEntity = mPhotoMapper.transformToEntity(user.getPhoto());
        entity.setPhotoEntity(photoEntity);
        return entity;
    }

    public User transformFromEntity(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUserName(entity.getUserName());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setDateJoined(entity.getDateJoined());
        user.setDateLastLogin(entity.getDateLastLogin());
        user.setSuperuser(entity.isSuperuser());
        user.setStaff(entity.isStaff());
        user.setActive(entity.isActive());
        user.setSubscribed(entity.isSubscribed());
        user.setVerified(entity.isVerified());
        user.setVatExempt(entity.isVatExempt());
        user.setAvgRating(entity.getAvgRating());
        Photo photo = mPhotoMapper.transformFromEntity(entity.getPhotoEntity());
        user.setPhoto(photo);
        return user;
    }
}
