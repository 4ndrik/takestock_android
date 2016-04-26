package com.devabit.takestock.rest;

import com.devabit.takestock.BuildConfig;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public interface RestApi {

    String API_BASE_URL = BuildConfig.API_URL;

    String POST_TOKEN_AUTH = "token/auth/";
    String POST_TOKEN_VERIFY = "token/verify/";

    String GET_CATEGORY = "category/";
    String GET_ADVERTS = "adverts/";
    String GET_SIZE_TYPES = "size_types/";
    String GET_CERTIFICATIONS = "certifications/";
    String GET_SHIPPING = "shipping/";
    String GET_CONDITIONS = "conditions/";

    String composeUrl(String... params);
}
