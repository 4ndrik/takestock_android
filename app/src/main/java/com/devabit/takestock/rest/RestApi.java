package com.devabit.takestock.rest;

import com.devabit.takestock.BuildConfig;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface RestApi {

    String SCHEME = "http";
    String API_BASE_URL = BuildConfig.API_URL;

    String POST_TOKEN_AUTH = "token/auth/";
    String POST_TOKEN_VERIFY = "token/verify/";
    String POST_TOKEN_REGISTER = "token/register/";

    String GET_CATEGORY = "category/";
    String GET_ADVERTS = "adverts/";
    String GET_SIZE_TYPES = "size_types/";
    String GET_CERTIFICATIONS = "certifications/";
    String GET_SHIPPING = "shipping/";
    String GET_CONDITIONS = "conditions/";
    String GET_PACKAGING = "packaging/";

    String composeUrl(String... params);
}
