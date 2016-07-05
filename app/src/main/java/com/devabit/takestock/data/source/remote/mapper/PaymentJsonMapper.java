package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Payment;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 05/07/2016.
 */
public class PaymentJsonMapper implements JsonMapper<Payment> {

    @Override public Payment fromJsonString(String json) throws Exception {
        return null;
    }

    @Override public String toJsonString(Payment target) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("offer_id", target.getOfferId());
        jsonObject.put("token", target.getTokenId());
        return jsonObject.toString();
    }
}
