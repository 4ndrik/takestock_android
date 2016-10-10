package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.UserCredentials;

/**
 * Created by Victor Artemyev on 09/10/2016.
 */

public class SignInJson implements JsonModel {
    public String username;
    public String password;

    public SignInJson(UserCredentials credentials) {
        this.password = credentials.password;
        this.username = credentials.userName;
    }
}
