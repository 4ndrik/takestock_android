package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Question;

/**
 * Created by Victor Artemyev on 20/09/2016.
 */

public class QuestionJson implements JsonModel {

    public int id;
    public int  user;
    public String message;
    public int advert;
    public String created_at;
    public boolean is_new;
    public String user_username;
    public AnswerJson answer;

    public Question toQuestion() {
        return new Question.Builder()
                .setId(id)
                .setUserId(user)
                .setMessage(message)
                .setAdvertId(advert)
                .setCreatedAt(created_at)
                .setIsNew(is_new)
                .setUserName(user_username)
                .setAnswer(answer == null ? null : answer.toAnswer())
                .create();
    }
}
