package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class UserPaginatedListJsonMapper extends PaginatedListJsonMapper<User> {

    @Override public PaginatedList<User> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        PaginatedList<User> paginatedList = getResultList(jsonObject);
        List<User> users = new UserJsonMapper()
                .fromJsonStringToList(jsonObject.getString(RESULTS));
        paginatedList.setResults(users);
        return paginatedList;
    }
}
