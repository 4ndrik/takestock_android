package com.devabit.takestock.data.source.remote;

import com.devabit.takestock.BuildConfig;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
interface ApiRest {

    String API_BASE_URL = BuildConfig.API_URL;

    String TOKEN_AUTH = API_BASE_URL + "/token/auth/";
    String TOKEN_VERIFY = API_BASE_URL + "/token/verify/";
    String TOKEN_REGISTER = API_BASE_URL + "/token/register/";

    String CATEGORY = API_BASE_URL + "/category/";
    String SIZE_TYPES = API_BASE_URL + "/size_types/";
    String CERTIFICATIONS = API_BASE_URL + "/certifications/";
    String SHIPPING = API_BASE_URL + "/shipping/";
    String CONDITIONS = API_BASE_URL + "/conditions/";
    String PACKAGING = API_BASE_URL + "/packaging/";
    String OFFER_STATUS = API_BASE_URL + "/offer_status/";
    String BUSINESS_TYPE = API_BASE_URL + "/a/businesstype/";
    String ADVERTS = API_BASE_URL + "/adverts/";
    String OFFERS = API_BASE_URL + "/offers/";
    String ACCEPT_OFFER = API_BASE_URL + "/accept_offer/";
    String ANSWERS = API_BASE_URL + "/qa/answers/";
    String QUESTIONS = API_BASE_URL + "/qa/questions/";
    String USERS = API_BASE_URL + "/users/";
    String ME = API_BASE_URL + "/me/";
    String WATCHLIST = API_BASE_URL + "/to_watchlist/";
    String PAY = API_BASE_URL + "/pay/";
}
