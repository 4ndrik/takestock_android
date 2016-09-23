package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Answer;

/**
 * Created by Victor Artemyev on 20/09/2016.
 */

public class AnswerJson implements JsonModel {

    public int id;
    public int user;
    public String message;
    public String created_at;
    public String user_username;
    public int[] question;

    public Answer toAnswer() {
        return new Answer.Builder()
                .setId(id)
                .setUserId(user)
                .setMessage(message)
                .setCreatedAt(created_at)
                .setUserName(user_username)
                .setQuestion(question)
                .create();
    }
}
