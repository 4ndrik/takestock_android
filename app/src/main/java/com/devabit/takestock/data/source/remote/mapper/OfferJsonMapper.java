package com.devabit.takestock.data.source.remote.mapper;

import android.text.TextUtils;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferJsonMapper implements JsonMapper<Offer> {

    private static final String ID = "id";
    private static final String COUNTER_OFFER_ID = "offer";
    private static final String ADVERT_ID = "advert";
    private static final String PRICE = "price";
    private static final String QUANTITY = "quantity";
    private static final String USER_ID = "user";
    private static final String COMMENT = "comment";
    private static final String STATUS_COMMENT = "accept_comment";
    private static final String DATE_CREATED = "created_at";
    private static final String DATE_UPDATED = "updated_at";
    private static final String USER = "user_detailed";
    private static final String STATUS_ID = "status";

    private final UserJsonMapper mUserMapper;

    public OfferJsonMapper() {
        mUserMapper = new UserJsonMapper();
    }

    public List<Offer> fromJsonStringToList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Offer> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            String jsonString = jsonArray.getString(index);
            Offer offer = fromJsonString(jsonString);
            result.add(offer);
        }
        return result;
    }

    @Override public Offer fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Offer offer = new Offer();
        offer.setId(jsonObject.getInt(ID));
        if (!jsonObject.isNull(COUNTER_OFFER_ID)) offer.setCounterOfferId(jsonObject.getInt(COUNTER_OFFER_ID));
        offer.setAdvertId(jsonObject.getInt(ADVERT_ID));
        offer.setPrice(jsonObject.getString(PRICE));
        offer.setQuantity(jsonObject.getInt(QUANTITY));
        offer.setUserId(jsonObject.getInt(USER_ID));
        offer.setOfferStatusId(jsonObject.getInt(STATUS_ID));
        offer.setComment(jsonObject.getString(COMMENT));
        offer.setStatusComment(jsonObject.isNull(STATUS_COMMENT) ? "" : jsonObject.getString(STATUS_COMMENT));
        offer.setDateCreated(jsonObject.getString(DATE_CREATED));
        offer.setDateUpdated(jsonObject.getString(DATE_UPDATED));
        User user = mUserMapper.fromJsonString(jsonObject.getString(USER));
        offer.setUser(user);
        return offer;
    }

    @Override public String toJsonString(Offer target) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ADVERT_ID, target.getAdvertId());
        jsonObject.put(USER_ID, target.getUserId());
        jsonObject.put(STATUS_ID, target.getOfferStatusId());

        if (target.getCounterOfferId() > 0) jsonObject.put(COUNTER_OFFER_ID, target.getCounterOfferId());

        String price = target.getPrice();
        if (!TextUtils.isEmpty(price)) jsonObject.put(PRICE, target.getPrice());

        int quantity = target.getQuantity();
        if (quantity > 0) jsonObject.put(QUANTITY, target.getQuantity());

        String comment = target.getComment();
        if (!TextUtils.isEmpty(comment)) jsonObject.put(COMMENT, comment);
        return jsonObject.toString();
    }
}
