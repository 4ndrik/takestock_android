package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.source.local.entity.AnswerEntity;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class AnswerEntityDataMapper {

    public Answer transformFromEntity(AnswerEntity entity) {
        Answer answer = new Answer();
        answer.setId(entity.getId());
        answer.setUserId(entity.getUserId());
        answer.setMessage(entity.getMessage());
        answer.setDateCreated(entity.getDateCreated());
        answer.setUserName(entity.getUserName());
        return answer;
    }

    public AnswerEntity transformToEntity(Answer answer) {
        AnswerEntity entity = new AnswerEntity();
        entity.setId(answer.getId());
        entity.setUserId(answer.getUserId());
        entity.setMessage(answer.getMessage());
        entity.setDateCreated(answer.getDateCreated());
        entity.setUserName(answer.getUserName());
        return entity;
    }
}
