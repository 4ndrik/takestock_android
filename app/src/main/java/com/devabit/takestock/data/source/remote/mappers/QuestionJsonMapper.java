package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionJsonMapper implements JsonMapper<Question> {

    private static final String ID = "id";
    private static final String USER = "user";
    private static final String MESSAGE = "message";
    private static final String ADVERT = "advert";
    private static final String CREATED_AT = "created_at";
    private static final String IS_NEW = "is_new";
    private static final String USER_USERNAME = "user_username";
    private static final String ANSWER = "answer";
    private final AnswerJsonMapper mAnswerMapper;

    public QuestionJsonMapper() {
        mAnswerMapper = new AnswerJsonMapper();
    }


    public List<Question> fromJsonStringToList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Question> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            String jsonString = jsonArray.getString(index);
            Question question = fromJsonString(jsonString);
            result.add(question);
        }
        return result;
    }

    @Override public Question fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Question question = new Question();
        question.setId(jsonObject.getInt(ID));
        question.setUserId(jsonObject.getInt(USER));
        question.setMessage(jsonObject.getString(MESSAGE));
        question.setAdvertId(jsonObject.getInt(ADVERT));
        question.setDateCreated(jsonObject.getString(CREATED_AT));
        question.setNew(jsonObject.getBoolean(IS_NEW));
        question.setUserName(jsonObject.getString(USER_USERNAME));
        if (!jsonObject.isNull(ANSWER)) {
            Answer answer = mAnswerMapper.fromJsonString(jsonObject.getString(ANSWER));
            question.setAnswer(answer);
        }
        return question;
    }

    @Override public String toJsonString(Question target) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER, target.getUserId());
        jsonObject.put(MESSAGE, target.getMessage());
        jsonObject.put(ADVERT, target.getAdvertId());
        jsonObject.put(IS_NEW, target.isNew());
        return jsonObject.toString();
    }
}
