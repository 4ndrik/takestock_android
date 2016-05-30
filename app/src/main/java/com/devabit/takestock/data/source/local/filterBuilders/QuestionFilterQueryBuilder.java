package com.devabit.takestock.data.source.local.filterBuilders;

import com.devabit.takestock.data.filters.QuestionFilter;
import com.devabit.takestock.data.source.local.entity.AdvertEntity;
import com.devabit.takestock.data.source.local.entity.QuestionEntity;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionFilterQueryBuilder extends FilterQueryBuilder<QuestionEntity, QuestionFilter> {

    @Override public RealmQuery<QuestionEntity> buildQuery(QuestionFilter filter) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<AdvertEntity> query = realm.where(AdvertEntity.class);
        int advertId = filter.getAdvertId();
        if (advertId > 0) {
            query.equalTo("mAdvertId", advertId);
        }
        query.findAll();
        return null;
    }
}
