package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class UserJsonMapper implements FromJsonMapper<User> {

    @Override public User fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        User user = new User();
        user.setId(jsonObject.getInt("id"));
        user.setUserName(jsonObject.getString("username"));
        user.setFirstName(jsonObject.getString("first_name"));
        user.setLastName(jsonObject.getString("last_name"));
        user.setEmail(jsonObject.getString("email"));
        user.setDateJoined(jsonObject.getString("date_joined"));
        user.setDateLastLogin(jsonObject.getString("last_login"));
        user.setSuperuser(jsonObject.getBoolean("is_superuser"));
        user.setSubscribed(jsonObject.getBoolean("is_subscribed"));
        user.setStaff(jsonObject.getBoolean("is_staff"));
        user.setActive(jsonObject.getBoolean("is_active"));
        user.setVatExempt(jsonObject.getBoolean("is_vat_exempt"));
        user.setVerified(jsonObject.getBoolean("is_verified"));
        user.setAvgRating(jsonObject.getDouble("avg_rating"));
        user.setPhoto(null);
        return user;
    }
}
