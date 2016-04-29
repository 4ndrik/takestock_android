package com.devabit.takestock.data.model;

import android.support.annotation.NonNull;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */

public class Size extends RealmObject {

    @PrimaryKey
    public String type;

    public Size(@NonNull String type) {
        this.type = type;
    }

    @Override public String toString() {
        return "Size{" +
                "type='" + type + '\'' +
                '}';
    }
}
