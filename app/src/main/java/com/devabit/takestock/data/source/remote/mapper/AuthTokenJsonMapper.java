package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.AuthToken;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class AuthTokenJsonMapper implements FromJsonMapper<AuthToken>, ToJsonMapper<AuthToken>{

    private static final String TOKEN = "token";

    @Override public AuthToken fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        AuthToken accessToken = new AuthToken();
        accessToken.token = jsonObject.getString(TOKEN);
        return accessToken;
    }

    @Override public String toJsonString(AuthToken authToken) throws JSONException {
        JSONObject  json = new JSONObject();
        json.put(TOKEN, authToken.token);
        return json.toString();
    }
}
