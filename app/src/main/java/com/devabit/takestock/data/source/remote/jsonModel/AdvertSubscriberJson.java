package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Advert;

/**
 * Created by Victor Artemyev on 16/09/2016.
 */
public class AdvertSubscriberJson {

    public int item_id;
    public String status;

    public AdvertSubscriberJson(int advertId) {
        this.item_id = advertId;
    }

    public Advert.Subscriber toSubscriber() {
        return new Advert.Subscriber(item_id, status);
    }
}
