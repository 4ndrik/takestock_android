package com.devabit.takestock.data.source.local.realmModel;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Size;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */

public class SizeRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mType;

    public SizeRealm() {}

    public SizeRealm(Size size) {
        mId = size.getId();
        mType = size.getType();
    }

    public void setType(@NonNull String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public Size getSize() {
        return new Size(mId, mType);
    }
}
