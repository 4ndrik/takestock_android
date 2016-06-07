package com.devabit.takestock.data.source.local.filterBuilders;

import com.devabit.takestock.data.filters.UserFilter;
import com.devabit.takestock.data.source.local.entity.UserEntity;
import io.realm.Realm;
import io.realm.RealmQuery;

import java.util.Set;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class UserFilterQueryBuilder extends FilterQueryBuilder<UserEntity, UserFilter> {

    @Override public RealmQuery<UserEntity> buildQuery(UserFilter filter) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<UserEntity> query = realm.where(UserEntity.class);
        Set<Integer> ids = filter.getUserIds();
        for (Integer id : ids) {
            query.equalTo("mId", id);
        }
        query.findAll();
        return query;
    }
}
