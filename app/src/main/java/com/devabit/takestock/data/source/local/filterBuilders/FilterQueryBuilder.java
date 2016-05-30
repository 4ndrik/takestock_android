package com.devabit.takestock.data.source.local.filterBuilders;

import com.devabit.takestock.data.filters.Filter;
import io.realm.RealmObject;
import io.realm.RealmQuery;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public abstract class FilterQueryBuilder<R extends RealmObject, T extends Filter> {

    public abstract RealmQuery<R> buildQuery(T filter);
}
