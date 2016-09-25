package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 20/09/2016.
 */

public class OfferJson {

    public int id;
    public int advert;
    public int[] child_offers;
    public String price;
    public int quantity;
    public int user;
    public int status;
    public int status_for_buyer;
    public String comment;
    public String created_at;
    public String updated_at;
    public AuthorJson user_detailed;
    public int price_for_stripe;
    public boolean from_seller;
    public int last_offer;
    public int notifications;
    public int[] shipping;

    public Offer toOffer() {
        return new Offer.Builder()
                .setId(id)
                .setAdvertId(advert)
                .setPrice(price)
                .setQuantity(quantity)
                .setUserId(user)
                .setStatus(status)
                .setStatusForBuyer(status_for_buyer)
                .setComment(comment)
                .setCreatedAt(created_at)
                .setUpdatedAt(updated_at)
                .setAuthor(user_detailed.getAuthor())
                .setPriceForStripe(price_for_stripe)
                .setFromSeller(from_seller)
                .setLastOfferId(last_offer)
                .setNotifications(notifications)
                .setShipping(shipping)
                .create();
    }
}
