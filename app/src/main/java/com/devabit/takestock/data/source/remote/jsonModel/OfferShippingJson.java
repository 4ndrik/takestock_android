package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 05/10/2016.
 */

public class OfferShippingJson implements JsonModel {

    public int id;
    public String street;
    public String house;
    public String city;
    public int postcode;
    public String phone;
    public String arrival_date;
    public String pick_up_date;
    public String tracking;
    public String courier_name;
    public boolean from_seller;
    public boolean stock_in_transit;
    public int offer;

    public Offer.Shipping toShipping() {
        return new Offer.Shipping.Builder()
                .setId(id)
                .setStreet(street)
                .setHouse(house)
                .setCity(city)
                .setPhone(phone)
                .setArrivalDate(arrival_date)
                .setPickUpDate(pick_up_date)
                .setTracking(tracking)
                .setCourierName(courier_name)
                .setFromSeller(from_seller)
                .setStockInTransit(stock_in_transit)
                .setOfferId(offer)
                .create();
    }
}
