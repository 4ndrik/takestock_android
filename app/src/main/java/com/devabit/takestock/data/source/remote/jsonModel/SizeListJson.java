package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Size;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class SizeListJson implements JsonModel {

    private final List<Size> sizes;

    public SizeListJson(List<Size> sizes) {
        this.sizes = sizes;
    }

    public List<Size> getSizes() {
        return sizes;
    }
}
