package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 20/09/2016.
 */

public class OfferCreateJson implements JsonModel {

    public final int advert;
    public final int user;
    public final int status;
    public final String price;
    public final int quantity;

    public OfferCreateJson(Offer offer) {
        this.advert = offer.getAdvertId();
        this.user = offer.getUserId();
        this.status = offer.getStatus();
        this.price = offer.getPrice();
        this.quantity = offer.getQuantity();
    }
}
