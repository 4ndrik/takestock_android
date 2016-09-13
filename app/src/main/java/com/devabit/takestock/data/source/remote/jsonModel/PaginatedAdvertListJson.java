package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.PaginatedList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 13/09/2016.
 */
public class PaginatedAdvertListJson implements JsonModel {

    public int count;
    public String next;
    public String previous;
    public List<AdvertJson> results;

    public PaginatedList<Advert> toPaginatedList() {
        PaginatedList<Advert> list = new PaginatedList<>();
        list.setCount(count);
        list.setNext(next);
        list.setPrevious(previous);
        list.setResults(toAdvertList());
        return list;
    }

    private List<Advert> toAdvertList() {
        List<Advert> adverts = new ArrayList<>(results.size());
        for (AdvertJson json : results) {
            adverts.add(json.toAdvert());
        }
        return adverts;
    }
}
