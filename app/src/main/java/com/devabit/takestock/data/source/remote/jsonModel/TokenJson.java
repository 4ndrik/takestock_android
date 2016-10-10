package com.devabit.takestock.data.source.remote.jsonModel;

/**
 * Created by Victor Artemyev on 09/10/2016.
 */

public class TokenJson implements JsonModel {

    public final String token;

    public TokenJson(String token) {
        this.token = token;
    }
}
