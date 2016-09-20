package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Advert;

/**
 * Created by Victor Artemyev on 16/09/2016.
 */
public class AdvertSubscriberJson {

    public int advert_id;
    public String status;

    public AdvertSubscriberJson(int advertId) {
        this.advert_id = advertId;
    }

    public Advert.Subscriber toSubscriber() {
        return new Advert.Subscriber(advert_id, status);
    }
}
