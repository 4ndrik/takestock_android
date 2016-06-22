package com.devabit.takestock.data.source.remote.filterBuilder;

import android.text.TextUtils;
import com.devabit.takestock.data.filters.UserFilter;

import java.util.Set;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class UserFilterUrlBuilder extends FilterUrlBuilder<UserFilter> {

    private static final String IDS = "ids";

    public UserFilterUrlBuilder(String baseUrl, UserFilter filter) {
        super(baseUrl, filter);
    }

    @Override public String buildUrl() {
        Set<Integer> ids = mFilter.getUserIds();
        if (!ids.isEmpty()) {
            appendQueryParameter(IDS, TextUtils.join(",", ids));
        }
        return mBuilder.toString();
    }
}
