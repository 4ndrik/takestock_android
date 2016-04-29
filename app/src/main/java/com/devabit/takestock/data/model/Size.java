package com.devabit.takestock.data.model;

import android.support.annotation.NonNull;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */

public class Size extends RealmObject {

    @PrimaryKey
    private String type;

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override public String toString() {
        return "Size{" +
                "type='" + type + '\'' +
                '}';
    }
}
