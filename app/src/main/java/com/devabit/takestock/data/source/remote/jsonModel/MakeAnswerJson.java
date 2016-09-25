package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Answer;

/**
 * Created by Victor Artemyev on 23/09/2016.
 */

public class MakeAnswerJson implements JsonModel {

    public int user;
    public String message;
    public int[] question;

    public MakeAnswerJson(Answer answer) {
        this.user = answer.getUserId();
        this.message = answer.getMessage();
        this.question = answer.getQuestion();
    }
}
