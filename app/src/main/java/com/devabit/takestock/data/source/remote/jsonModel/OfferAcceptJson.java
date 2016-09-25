package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 24/09/2016.
 */

public class OfferAcceptJson implements JsonModel {

    public int id;
    public int user;
    public String street;
    public String house;
    public String city;
    public String postcode;
    public String phone;
    public String arrival_date;
    public String pick_up_date;
    public String tracking;
    public String courier_name;
    public boolean from_seller;
    public boolean stock_in_transit;
    public int status;
    public String comment;
    public String price;
    public int quantity;
    public int offer;

    public Offer.Accept toOfferAccept() {
        return new Offer.Accept.Builder()
                .setId(id)
                .setUserId(user)
                .setOfferId(offer)
                .setStatus(status)
                .setStreet(street)
                .setHouse(house)
                .setCity(city)
                .setPostcode(postcode)
                .setPhone(phone)
                .setArrivalDate(arrival_date)
                .setPickUpDate(pick_up_date)
                .setTracking(tracking)
                .setCourierName(courier_name)
                .setFromSeller(from_seller)
                .setStockInTransit(stock_in_transit)
                .setComment(comment)
                .setPrice(price)
                .setQuantity(quantity)
                .create();
    }
}
