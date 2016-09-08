package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.OfferStatus;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class OfferStatusListJson implements JsonModel {

    private final List<OfferStatus> offerStatuses;

    public OfferStatusListJson(List<OfferStatus> offerStatuses) {
        this.offerStatuses = offerStatuses;
    }

    public List<OfferStatus> getOfferStatuses() {
        return offerStatuses;
    }
}
