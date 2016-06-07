package com.devabit.takestock.data.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class UserFilter implements Filter {

    private Set<Integer> mUserIds = new HashSet<>();

    public void setUserId(int id) {
        mUserIds.add(id);
    }

    public void setUserIds(List<Integer> userIds) {
        mUserIds.addAll(userIds);
    }

    public Set<Integer> getUserIds() {
        return mUserIds;
    }
}
