package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.data.source.remote.ApiRest;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class PhotoJson implements JsonModel {

    public int id;
    public String image;
    public String thumbnail;
    public String thumb_large;
    public boolean is_main;
    public String width;
    public String height;

    public Photo toPhoto() {
        return new Photo.Builder()
                .setId(id)
                .setImage(ApiRest.BASE_URL + image)
                .setThumbnail(ApiRest.BASE_URL + thumbnail)
                .setThumbnailLarge(ApiRest.BASE_URL + thumb_large)
                .setIsMain(is_main)
                .setWidth(Integer.valueOf(width))
                .setHeight(Integer.valueOf(height))
                .build();
    }
}
