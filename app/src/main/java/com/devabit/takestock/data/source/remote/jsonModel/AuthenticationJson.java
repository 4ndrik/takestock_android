package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Authentication;

/**
 * Created by Victor Artemyev on 09/10/2016.
 */

public class AuthenticationJson implements JsonModel {

    public String email;
    public String username;
    public int id;
    public String token;
    public UserJson user;

    public Authentication toAuthentication() {
        return new Authentication.Builder()
                .setToken(token)
                .setUserId(user == null ? id : user.id)
                .setUsername(user == null ? username : user.username)
                .setEmail(user == null ? email : user.email)
                .setUser(user == null ? null : user.toUser())
                .create();
    }
}
