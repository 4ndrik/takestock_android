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

    public Answer toAnswer() {
        Answer answer = new Answer();
        answer.setId(id);
        answer.setUserId(user);
        answer.setMessage(message);
        answer.setDateCreated(created_at);
        return answer;
    }

}
