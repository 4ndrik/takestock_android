package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Condition;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class ConditionRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mState;

    public ConditionRealm() {}

    public ConditionRealm(Condition condition) {
        mId = condition.getId();
        mState = condition.getState();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Condition getCondition() {
        return new Condition(mId, mState);
    }

}
