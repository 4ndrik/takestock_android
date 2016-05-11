package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Author;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AuthorJsonMapper implements FromJsonMapper<Author> {

    @Override public Author fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Author author = new Author();
        author.setId(jsonObject.getInt("id"));
        author.setUserName(jsonObject.getString("username"));
        author.setFirstName(jsonObject.getString("first_name"));
        author.setLastName(jsonObject.getString("last_name"));
        author.setEmail(jsonObject.getString("email"));
        author.setDateJoined(jsonObject.getString("date_joined"));
        author.setDateLastLogin(jsonObject.getString("last_login"));
        author.setSuperuser(jsonObject.getBoolean("is_superuser"));
        author.setSubscribed(jsonObject.getBoolean("is_subscribed"));
        author.setStaff(jsonObject.getBoolean("is_staff"));
        author.setActive(jsonObject.getBoolean("is_active"));
        author.setVatExempt(jsonObject.getBoolean("is_vat_exempt"));
        author.setVerified(jsonObject.getBoolean("is_verified"));
        author.setAvgRating(jsonObject.getDouble("avg_rating"));
        author.setPhoto(null);
        return author;
    }
}
