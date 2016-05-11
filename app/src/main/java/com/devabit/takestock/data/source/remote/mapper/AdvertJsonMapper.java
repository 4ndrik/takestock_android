package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Author;
import com.devabit.takestock.data.model.Photo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertJsonMapper implements FromJsonMapper<List<Advert>> {

    private static final String ID = "id";

    private final AuthorJsonMapper mAuthorMapper;
    private final PhotoJsonMapper mPhotoMapper;

    public AdvertJsonMapper() {
        mAuthorMapper = new AuthorJsonMapper();
        mPhotoMapper = new PhotoJsonMapper();
    }

    @Override public List<Advert> fromJsonString(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Advert> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);

            Advert advert = new Advert();
            advert.setId(jsonObject.getInt(ID));
            advert.setName(jsonObject.getString("name"));
            advert.setDateCreatedAt(jsonObject.getString("created_at"));
            advert.setDateExpiresAt(jsonObject.getString("expires_at"));
            advert.setDateUpdatedAt(jsonObject.getString("updated_at"));
            advert.setIntendedUse(jsonObject.getString("intended_use"));
            advert.setGuidePrice(jsonObject.getString("guide_price"));
            advert.setDescription(jsonObject.getString("description"));
            advert.setLocation(jsonObject.getString("location"));
            advert.setShippingId(jsonObject.getInt("shipping"));
            advert.setVatExempt(jsonObject.getBoolean("is_vat_exempt"));
            advert.setAuthorId(jsonObject.getInt("author"));
            advert.setCategoryId(jsonObject.getInt("category"));
            if (!jsonObject.isNull("subcategory")) advert.setSubCategoryId(jsonObject.getInt("subcategory"));
            if (!jsonObject.isNull("packaging")) advert.setPackagingId(jsonObject.getInt("packaging"));
            advert.setMinOrderQuantity(jsonObject.getInt("min_order_quantity"));
            advert.setSize(jsonObject.getString("size"));
            advert.setCertificationId(jsonObject.getInt("certification"));
            advert.setCertificationExtra(jsonObject.getString("certification_extra"));
            advert.setConditionId(jsonObject.getInt("condition"));
            advert.setItemsCount(jsonObject.getInt("items_count"));

            JSONArray jsonTagsArray = jsonObject.getJSONArray("tags");
            List<String> tags = new ArrayList<>(jsonTagsArray.length());
            for (int i = 0; i < jsonTagsArray.length(); i++) {
                tags.add(jsonTagsArray.getString(i));
            }
            advert.setTags(tags);

            Author author = mAuthorMapper.fromJsonString(jsonObject.getString("author_detailed"));
            advert.setAuthor(author);

            JSONArray jsonPhotosArray = jsonObject.getJSONArray("photos");
            List<Photo> photos = new ArrayList<>(jsonPhotosArray.length());
            for (int i = 0; i < jsonPhotosArray.length(); i++) {
                photos.add(mPhotoMapper.fromJsonString(jsonPhotosArray.getString(i)));
            }
            advert.setPhotos(photos);
            result.add(advert);
        }
        return result;
    }
}
