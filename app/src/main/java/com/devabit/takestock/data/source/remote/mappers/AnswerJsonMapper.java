package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Answer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class AnswerJsonMapper implements JsonMapper<Answer> {

    private static final String ID = "id";
    private static final String USER = "user";
    private static final String MESSAGE = "message";
    private static final String CREATED_AT = "created_at";
    private static final String QUESTION_SET = "question_set";

    @Override public Answer fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Answer answer = new Answer();
        answer.setId(jsonObject.getInt(ID));
        answer.setUserId(jsonObject.getInt(USER));
        answer.setMessage(jsonObject.getString(MESSAGE));
        answer.setDateCreated(jsonObject.getString(CREATED_AT));
        return answer;
    }

    @Override public String toJsonString(Answer target) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER, target.getUserId());
        jsonObject.put(MESSAGE, target.getMessage());

        JSONArray jsonArray = new JSONArray();
        for (int questionId : target.getQuestionSet()){
            jsonArray.put(questionId);
        }
        jsonObject.put(QUESTION_SET, jsonArray);
        return jsonObject.toString();
    }
}
