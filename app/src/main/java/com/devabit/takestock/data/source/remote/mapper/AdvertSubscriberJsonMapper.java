package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.AdvertSubscriber;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 29/06/2016.
 */
public class AdvertSubscriberJsonMapper implements JsonMapper<AdvertSubscriber> {

    private static final String ADVERT_ID = "advert_id";
    private static final String STATUS = "status";

    @Override public AdvertSubscriber fromJsonString(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        AdvertSubscriber subscriber = new AdvertSubscriber();
        subscriber.setAdvertId(jsonObject.getInt(ADVERT_ID));
        subscriber.setStatus(jsonObject.getString(STATUS));
        return subscriber;
    }

    @Override public String toJsonString(AdvertSubscriber target) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ADVERT_ID, target.getAdvertId());
        return jsonObject.toString();
    }
}
