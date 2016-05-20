package com.devabit.takestock.data.models;

import android.support.annotation.NonNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */

public class Size {

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
