package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class UserJsonMapper implements JsonMapper<User> {

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String DATE_JOINED = "date_joined";
    private static final String LAST_LOGIN = "last_login";
    private static final String IS_SUPERUSER = "is_superuser";
    private static final String IS_SUBSCRIBED = "is_subscribed";
    private static final String IS_STAFF = "is_staff";
    private static final String IS_ACTIVE = "is_active";
    private static final String IS_VAT_EXEMPT = "is_vat_exempt";
    private static final String IS_VERIFIED = "is_verified";
    private static final String AVG_RATING = "avg_rating";
    private static final String PHOTO = "photo";

    public List<User> fromJsonStringToList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<User> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            String jsonString = jsonArray.getString(index);
            User user = fromJsonString(jsonString);
            result.add(user);
        }
        return result;
    }

    @Override public User fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        User user = new User();
        user.setId(jsonObject.getInt(ID));
        user.setUserName(jsonObject.getString(USERNAME));
        user.setFirstName(jsonObject.getString(FIRST_NAME));
        user.setLastName(jsonObject.getString(LAST_NAME));
        user.setEmail(jsonObject.getString(EMAIL));
        user.setDateJoined(jsonObject.getString(DATE_JOINED));
        user.setDateLastLogin(jsonObject.getString(LAST_LOGIN));
        user.setSuperuser(jsonObject.getBoolean(IS_SUPERUSER));
        user.setSubscribed(jsonObject.getBoolean(IS_SUBSCRIBED));
        user.setStaff(jsonObject.getBoolean(IS_STAFF));
        user.setActive(jsonObject.getBoolean(IS_ACTIVE));
        user.setVatExempt(jsonObject.getBoolean(IS_VAT_EXEMPT));
        user.setVerified(jsonObject.getBoolean(IS_VERIFIED));
        user.setAvgRating(jsonObject.getDouble(AVG_RATING));
        if (jsonObject.isNull(PHOTO)) {
            user.setPhotoPath("");
        } else {
            user.setPhotoPath(jsonObject.getString(PHOTO));
        }
        return user;
    }

    @Override public String toJsonString(User target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
