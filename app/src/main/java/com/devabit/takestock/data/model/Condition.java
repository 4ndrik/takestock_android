package com.devabit.takestock.data.model;

import android.support.annotation.NonNull;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Condition extends RealmObject {

    @PrimaryKey
    public String state;

    public Condition(@NonNull String state) {
        this.state = state;
    }

    @Override public String toString() {
        return "Condition{" +
                "state='" + state + '\'' +
                '}';
    }
}
