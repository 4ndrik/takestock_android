package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Shipping;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class ShippingListJson implements JsonModel {

    private final List<Shipping> shippings;

    public ShippingListJson(List<Shipping> shippings) {
        this.shippings = shippings;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }
}
