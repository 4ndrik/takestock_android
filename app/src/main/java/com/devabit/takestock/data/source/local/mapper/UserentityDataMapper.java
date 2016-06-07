package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.User;
import com.devabit.takestock.data.source.local.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class UserEntityDataMapper {

    public List<User> transformFromEntitiesToList(List<UserEntity> entities) {
        List<User> result = new ArrayList<>(entities.size());
        for (UserEntity entity : entities) {
            User user = transformFromEntity(entity);
            result.add(user);
        }
        return result;
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
        entity.setPhotoPath(user.getPhotoPath());
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
        user.setPhotoPath(entity.getPhotoPath());
        return user;
    }
}
