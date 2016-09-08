package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Size;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class SizeJson implements JsonModel {

    public int id;
    public String type;

    public Size getSize() {
        return new Size(id, type);
    }
}
