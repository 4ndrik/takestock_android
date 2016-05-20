package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.util.Encoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertJsonMapper implements FromJsonMapper<List<Advert>>, ToJsonMapper<Advert> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CREATED_AT = "created_at";
    private static final String EXPIRES_AT = "expires_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String INTENDED_USE = "intended_use";
    private static final String GUIDE_PRICE = "guide_price";
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String SHIPPING = "shipping";
    private static final String IS_VAT_EXEMPT = "is_vat_exempt";
    private static final String AUTHOR = "author";
    private static final String CATEGORY = "category";
    private static final String SUBCATEGORY = "subcategory";
    private static final String PACKAGING = "packaging";
    private static final String MIN_ORDER_QUANTITY = "min_order_quantity";
    private static final String SIZE = "size";
    private static final String CERTIFICATION = "certification";
    private static final String CERTIFICATION_EXTRA = "certification_extra";
    private static final String CONDITION = "condition";
    private static final String ITEMS_COUNT = "items_count";
    private static final String TAGS = "tags";
    private static final String AUTHOR_DETAILED = "author_detailed";
    private static final String PHOTOS = "photos";
    private static final String PHOTOS_LIST = "photos_list";

    private final UserJsonMapper mUserMapper;
    private final PhotoJsonMapper mPhotoMapper;

    public AdvertJsonMapper() {
        mUserMapper = new UserJsonMapper();
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
            advert.setName(jsonObject.getString(NAME));
            advert.setDateCreatedAt(jsonObject.getString(CREATED_AT));
            advert.setDateExpiresAt(jsonObject.getString(EXPIRES_AT));
            advert.setDateUpdatedAt(jsonObject.getString(UPDATED_AT));
            advert.setIntendedUse(jsonObject.getString(INTENDED_USE));
            advert.setGuidePrice(jsonObject.getString(GUIDE_PRICE));
            advert.setDescription(jsonObject.getString(DESCRIPTION));
            advert.setLocation(jsonObject.getString(LOCATION));
            if (!jsonObject.isNull(SHIPPING)) advert.setShippingId(jsonObject.getInt(SHIPPING));
            advert.setVatExempt(jsonObject.getBoolean(IS_VAT_EXEMPT));
            advert.setAuthorId(jsonObject.getInt(AUTHOR));
            advert.setCategoryId(jsonObject.getInt(CATEGORY));
            if (!jsonObject.isNull(SUBCATEGORY)) advert.setSubCategoryId(jsonObject.getInt(SUBCATEGORY));
            if (!jsonObject.isNull(PACKAGING)) advert.setPackagingId(jsonObject.getInt(PACKAGING));
            advert.setMinOrderQuantity(jsonObject.getInt(MIN_ORDER_QUANTITY));
            advert.setSize(jsonObject.getString(SIZE));
            if (!jsonObject.isNull(CERTIFICATION)) advert.setCertificationId(jsonObject.getInt(CERTIFICATION));
            advert.setCertificationExtra(jsonObject.getString(CERTIFICATION_EXTRA));
            if (!jsonObject.isNull(CONDITION)) advert.setConditionId(jsonObject.getInt(CONDITION));
            advert.setItemsCount(jsonObject.getInt(ITEMS_COUNT));

            JSONArray jsonTagsArray = jsonObject.getJSONArray(TAGS);
            List<String> tags = new ArrayList<>(jsonTagsArray.length());
            for (int i = 0; i < jsonTagsArray.length(); i++) {
                tags.add(jsonTagsArray.getString(i));
            }
            advert.setTags(tags);

            User user = mUserMapper.fromJsonString(jsonObject.getString(AUTHOR_DETAILED));
            advert.setUser(user);

            JSONArray jsonPhotosArray = jsonObject.getJSONArray(PHOTOS);
            List<Photo> photos = new ArrayList<>(jsonPhotosArray.length());
            for (int i = 0; i < jsonPhotosArray.length(); i++) {
                photos.add(mPhotoMapper.fromJsonString(jsonPhotosArray.getString(i)));
            }
            advert.setPhotos(photos);
            result.add(advert);
        }
        return result;
    }

    @Override public String toJsonString(Advert target) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NAME, target.getName());
        jsonObject.put(EXPIRES_AT, target.getDateExpiresAt());
        jsonObject.put(GUIDE_PRICE, target.getGuidePrice());
        jsonObject.put(DESCRIPTION, target.getDescription());
        jsonObject.put(LOCATION, target.getLocation());
        jsonObject.put(SHIPPING, target.getShippingId());
        jsonObject.put(AUTHOR, target.getAuthorId());
        jsonObject.put(CATEGORY, target.getCategoryId());
        jsonObject.put(SUBCATEGORY, target.getSubCategoryId());
        jsonObject.put(PACKAGING, target.getPackagingId());
        jsonObject.put(MIN_ORDER_QUANTITY, target.getMinOrderQuantity());
        jsonObject.put(SIZE, target.getSize());
        jsonObject.put(CERTIFICATION, target.getCertificationId());
        jsonObject.put(CERTIFICATION_EXTRA, target.getCertificationExtra());
        jsonObject.put(CONDITION, target.getConditionId());
        jsonObject.put(ITEMS_COUNT, target.getItemsCount());
        JSONArray array = new JSONArray();
        array.put("tag");
        array.put("tag");
        jsonObject.put(TAGS, array);
        JSONArray jsonArray = new JSONArray();
        for(Photo photo : target.getPhotos()) {
            try {
                jsonArray.put("data:image/jpg;base64," + Encoder.encodeFileToBase64(photo.getImagePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        jsonObject.put(PHOTOS_LIST, jsonArray);
        return jsonObject.toString();
    }
}
