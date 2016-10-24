package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Payment;

/**
 * Created by Victor Artemyev on 04/10/2016.
 */

public class PaymentJson implements JsonModel {

    public int offer_id;
    public String token;
    public String payment_type;

    public PaymentJson(Payment payment) {
        this.offer_id = payment.getOfferId();
        this.token = payment.getTokenId();
        this.payment_type = payment.getType();
    }
}
