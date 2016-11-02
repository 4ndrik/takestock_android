package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Offer;

/**
 * Created by Victor Artemyev on 25/09/2016.
 */

public class MakeOfferAcceptJson implements JsonModel {

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

    public MakeOfferAcceptJson(Offer.Accept accept) {
        this.street = accept.getStreet();
        this.house = accept.getHouse();
        this.city = accept.getCity();
        this.postcode = accept.getPostcode();
        this.phone = accept.getPhone();
        this.arrival_date = accept.getArrivalDate();
        this.pick_up_date = accept.getPickUpDate();
        this.tracking = accept.getTracking();
        this.courier_name = accept.getCourierName();
        this.from_seller = accept.isFromSeller();
        this.stock_in_transit = accept.isStockInTransit();
        this.status = accept.getStatus();
        this.comment = accept.getComment();
        this.price = accept.getPrice();
        this.quantity = accept.getQuantity();
        this.offer = accept.getOfferId();
    }
}
