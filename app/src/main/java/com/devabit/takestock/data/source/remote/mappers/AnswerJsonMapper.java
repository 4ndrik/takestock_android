package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Answer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class AnswerJsonMapper implements JsonMapper<Answer> {

    @Override public Answer fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Answer answer = new Answer();
        answer.setId(jsonObject.getInt("id"));
        answer.setUserId(jsonObject.getInt("user"));
        answer.setMessage(jsonObject.getString("message"));
        answer.setDateCreated(jsonObject.getString("created_at"));
        return answer;
    }

    @Override public String toJsonString(Answer target) throws JSONException {
        return null;
    }
}
