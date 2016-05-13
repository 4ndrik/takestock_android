package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Photo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class PhotoJsonMapper implements FromJsonMapper<Photo> {

    @Override public Photo fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Photo photo = new Photo();
        photo.setId(jsonObject.getInt("id"));
        photo.setImagePath(jsonObject.getString("image"));
        photo.setMain(jsonObject.getBoolean("is_main"));
        photo.setWidth(Integer.valueOf(jsonObject.getString("width")));
        photo.setHeight(Integer.valueOf(jsonObject.getString("height")));
        return photo;
    }
}
