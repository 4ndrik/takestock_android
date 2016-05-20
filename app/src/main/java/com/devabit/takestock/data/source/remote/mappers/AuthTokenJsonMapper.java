package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.AuthToken;
import com.devabit.takestock.data.models.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class AuthTokenJsonMapper implements FromJsonMapper<AuthToken>, ToJsonMapper<AuthToken>{

    private static final String TOKEN = "token";
    private static final String USER = "user";
    private static final String ID = "id";
    private static final String USERNAME= "username";
    private static final String EMAIL = "email";

    @Override public AuthToken fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        AuthToken accessToken = new AuthToken();

        if (jsonObject.has(USER)) {
            User user = new UserJsonMapper().fromJsonString(jsonObject.getString(USER));
            accessToken.userId = user.getId();
            accessToken.username = user.getUserName();
            accessToken.email = user.getEmail();
        } else {
            accessToken.userId = jsonObject.getInt(ID);
            accessToken.username = jsonObject.getString(USERNAME);
            accessToken.email = jsonObject.getString(EMAIL);
        }
        accessToken.token = jsonObject.getString(TOKEN);
        return accessToken;
    }

    @Override public String toJsonString(AuthToken authToken) throws JSONException {
        JSONObject  json = new JSONObject();
        json.put(TOKEN, authToken.token);
        return json.toString();
    }
}
