package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Condition;
import com.devabit.takestock.data.source.local.entity.ConditionEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class ConditionEntityDataMapper {

    public List<ConditionEntity> transformToEntityList(List<Condition> conditions) {
        List<ConditionEntity> result = new ArrayList<>(conditions.size());
        for (Condition condition : conditions) {
            ConditionEntity entity = transformToEntity(condition);
            result.add(entity);
        }
        return result;
    }

    public ConditionEntity transformToEntity(Condition condition) {
        ConditionEntity entity = new ConditionEntity();
        entity.setId(condition.getId());
        entity.setState(condition.getState());
        return entity;
    }

    public List<Condition> transformFromEntityList(RealmResults<ConditionEntity> conditionEntities) {
        List<Condition> result = new ArrayList<>(conditionEntities.size());
        for (ConditionEntity entity : conditionEntities) {
            Condition condition = transformFromEntity(entity);
            result.add(condition);
        }
        return result;
    }

    public Condition transformFromEntity(ConditionEntity entity) {
        if (entity == null) return null;
        Condition condition = new Condition();
        condition.setId(entity.getId());
        condition.setState(entity.getState());
        return condition;
    }
}
