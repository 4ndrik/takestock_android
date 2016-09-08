package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Condition;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class ConditionListJson {

    private final List<Condition> conditions;

    public ConditionListJson(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }
}
