package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.data.model.PaginatedList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionResultListJsonMapper extends ResultListJsonMapper<Question> {

    @Override public PaginatedList<Question> fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        PaginatedList<Question> paginatedList = getResultList(jsonObject);
        List<Question> questions = new QuestionJsonMapper()
                .fromJsonStringToList(jsonObject.getString(RESULTS));
        paginatedList.setResults(questions);
        return paginatedList;
    }
}
