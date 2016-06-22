package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.models.ResultList;
import com.devabit.takestock.data.models.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class UserResultListJsonMapper extends ResultListJsonMapper<User> {

    @Override public ResultList<User> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        ResultList<User> resultList = getResultList(jsonObject);
        List<User> users = new UserJsonMapper()
                .fromJsonStringToList(jsonObject.getString(RESULTS));
        resultList.setResults(users);
        return resultList;
    }
}
