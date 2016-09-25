package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.PaginatedList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 13/09/2016.
 */
public class PaginatedOfferListJson implements JsonModel {

    public int count;
    public String next;
    public String previous;
    public List<OfferJson> results;

    public PaginatedList<Offer> toPaginatedList() {
        PaginatedList<Offer> list = new PaginatedList<>();
        list.setCount(count);
        list.setNext(next);
        list.setPrevious(previous);
        list.setResults(toQuestionList());
        return list;
    }

    private List<Offer> toQuestionList() {
        List<Offer> questions = new ArrayList<>(results.size());
        for (OfferJson json : results) {
            questions.add(json.toOffer());
        }
        return questions;
    }
}
