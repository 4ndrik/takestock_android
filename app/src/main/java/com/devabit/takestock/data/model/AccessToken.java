package com.devabit.takestock.data.model;

import com.devabit.takestock.data.remote.RequestObject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public final class AccessToken implements RequestObject {

    private static final String TOKEN = "token";

    public String token;

    public static AccessToken fromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        AccessToken accessToken = new AccessToken();
        accessToken.token = jsonObject.getString(TOKEN);
        return accessToken;
    }

    @Override public String toJsonString() throws JSONException {
        JSONObject  json = new JSONObject();
        json.put(TOKEN, token);
        return json.toString();
    }

    @Override public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
