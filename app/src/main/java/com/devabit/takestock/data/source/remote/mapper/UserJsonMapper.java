package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.models.User;
import com.devabit.takestock.utils.Encoder;
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
    private static final String BUSINESS_NAME = "bussines_name";
    private static final String BUSINESS_TYPE = "bussines_type";
    private static final String BUSINESS_SUBTYPE = "business_sub_type";
    private static final String POSTCODE = "postcode";
    private static final String VAT_NUMBER = "vat_number";
    private static final String PHOTO_B64 = "photo_b64";

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
        user.setPhotoPath(jsonObject.isNull(PHOTO) ? "" : jsonObject.getString(PHOTO));
        user.setSubscribed(jsonObject.getBoolean(IS_SUBSCRIBED));

        user.setBusinessName(jsonObject.isNull(BUSINESS_NAME) ? "" : jsonObject.getString(BUSINESS_NAME));
        user.setBusinessTypeId(jsonObject.isNull(BUSINESS_TYPE) ? -1 : jsonObject.getInt(BUSINESS_TYPE));
        user.setBusinessSubtypeId(jsonObject.isNull(BUSINESS_SUBTYPE) ? -1 : jsonObject.getInt(BUSINESS_SUBTYPE));
        user.setPostcode(jsonObject.isNull(POSTCODE)? -1 : jsonObject.getInt(POSTCODE));
        user.setVatExempt(jsonObject.getBoolean(IS_VAT_EXEMPT));
        user.setVatNumber(jsonObject.isNull(VAT_NUMBER) ? -1 : jsonObject.getInt(VAT_NUMBER));

        user.setDateJoined(jsonObject.getString(DATE_JOINED));
        user.setDateLastLogin(jsonObject.getString(LAST_LOGIN));
        user.setSuperuser(jsonObject.getBoolean(IS_SUPERUSER));
        user.setStaff(jsonObject.getBoolean(IS_STAFF));
        user.setActive(jsonObject.getBoolean(IS_ACTIVE));
        user.setVerified(jsonObject.getBoolean(IS_VERIFIED));
        user.setAvgRating(jsonObject.getDouble(AVG_RATING));
        return user;
    }

    @Override public String toJsonString(User target) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, target.getId());
        jsonObject.put(USERNAME, target.getUserName());
        jsonObject.put(EMAIL, target.getEmail());
        jsonObject.put(IS_SUBSCRIBED, target.isSubscribed());
        if (!target.getBusinessName().isEmpty()) jsonObject.put(BUSINESS_NAME, target.getBusinessName());
        if (target.getBusinessTypeId() > 0) jsonObject.put(BUSINESS_TYPE, target.getBusinessTypeId());
        if (target.getBusinessSubtypeId() > 0) jsonObject.put(BUSINESS_SUBTYPE, target.getBusinessSubtypeId());
        if (target.getPostcode() > 0) jsonObject.put(POSTCODE, target.getPostcode());
        jsonObject.put(IS_VAT_EXEMPT, target.isVatExempt());
        if (target.getVatNumber() > 0) jsonObject.put(VAT_NUMBER, target.getVatNumber());
        if (!target.getPhotoPath().isEmpty()) {
            String photo = Encoder.encodeFileToBase64(target.getPhotoPath());
            jsonObject.put(PHOTO_B64, photo);
        }
        return jsonObject.toString();
    }
}
