package com.devabit.takestock.data.source.local.filterBuilders;

import com.devabit.takestock.data.filters.AdvertFilter;
import com.devabit.takestock.data.source.local.entity.AdvertEntity;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertFilterQueryBuilder extends FilterQueryBuilder<AdvertEntity, AdvertFilter>{

    public RealmQuery<AdvertEntity> buildQuery(AdvertFilter filter) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<AdvertEntity> query = realm.where(AdvertEntity.class);
        int authorId = filter.getAuthorId();
        if (authorId > 0) {
            query.equalTo("mAuthorId", authorId);
        }
        query.findAll();
        return query;
    }
}
