package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.OfferStatus;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class OfferStatusJson implements JsonModel {

    private final List<OfferStatus> offerStatuses;

    public OfferStatusJson(List<OfferStatus> offerStatuses) {
        this.offerStatuses = offerStatuses;
    }

    public List<OfferStatus> getOfferStatuses() {
        return offerStatuses;
    }
}
