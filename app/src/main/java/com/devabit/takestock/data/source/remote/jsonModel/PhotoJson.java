package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.BuildConfig;
import com.devabit.takestock.data.model.Photo;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class PhotoJson implements JsonModel {

    private static final String IMAGE_URL = BuildConfig.IMAGE_URL;

    public int id;
    public String image;
    public boolean is_main;
    public String width;
    public String height;

    public Photo toPhoto() {
        return new Photo.Builder()
                .setId(id)
                .setImage(IMAGE_URL + image)
                .setIsMain(is_main)
                .setWidth(Integer.valueOf(width))
                .setHeight(Integer.valueOf(height))
                .build();
    }
}
