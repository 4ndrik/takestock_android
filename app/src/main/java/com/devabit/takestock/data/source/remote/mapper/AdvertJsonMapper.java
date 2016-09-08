package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.utils.Encoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertJsonMapper implements JsonMapper<Advert> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CREATED_AT = "created_at";
    private static final String EXPIRES_AT = "expires_at";
    private static final String UPDATED_AT = "updated_at";
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
    private static final String CERTIFICATION_ID = "certification_id";
    private static final String CERTIFICATION_EXTRA = "certification_extra";
    private static final String CONDITION = "condition";
    private static final String ITEMS_COUNT = "items_count";
    private static final String TAGS = "tags";
    private static final String AUTHOR_DETAILED = "author_detailed";
    private static final String PHOTOS = "photos";
    private static final String PHOTOS_LIST = "photos_list";
    private static final String PACKAGING_NAME = "packaging_name";
    private static final String OFFERS_COUNT = "offers_count";
    private static final String DAYS_LEFT = "days_left";
    private static final String QUESTIONS_COUNT = "questions_count";
    private static final String SUBSCRIBERS = "subscribers";

    private final CertificationJsonMapper mCertificationMapper;
    private final UserJsonMapper mUserMapper;
    private final PhotoJsonMapper mPhotoMapper;

    public AdvertJsonMapper() {
        mCertificationMapper = new CertificationJsonMapper();
        mUserMapper = new UserJsonMapper();
        mPhotoMapper = new PhotoJsonMapper();
    }

    public List<Advert> fromJsonStringToList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Advert> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            String jsonString = jsonArray.getString(index);
            Advert advert = fromJsonString(jsonString);
            result.add(advert);
        }
        return result;
    }

    @Override public Advert fromJsonString(String json) throws JSONException {
//        JSONObject jsonObject = new JSONObject(json);
//
//        Advert advert = new Advert();
//        advert.setId(jsonObject.getInt(ID));
//        advert.setName(jsonObject.getString(NAME));
//        advert.setCreatedAt(jsonObject.getString(CREATED_AT));
//        advert.setExpiresAt(jsonObject.getString(EXPIRES_AT));
//        advert.setUpdatedAt(jsonObject.getString(UPDATED_AT));
//        advert.setGuidePrice(jsonObject.getString(GUIDE_PRICE));
//        advert.setDescription(jsonObject.getString(DESCRIPTION));
//        advert.setLocation(jsonObject.getString(LOCATION));
//        if (!jsonObject.isNull(SHIPPING)) advert.setShippingId(jsonObject.getInt(SHIPPING));
//        advert.setVatExempt(jsonObject.getBoolean(IS_VAT_EXEMPT));
//        advert.setAuthorId(jsonObject.getInt(AUTHOR));
//        advert.setCategoryId(jsonObject.getInt(CATEGORY));
//        if (!jsonObject.isNull(SUBCATEGORY)) advert.setSubCategoryId(jsonObject.getInt(SUBCATEGORY));
//        if (!jsonObject.isNull(PACKAGING)) advert.setPackagingId(jsonObject.getInt(PACKAGING));
//        advert.setMinOrderQuantity(jsonObject.getInt(MIN_ORDER_QUANTITY));
//        advert.setSize(jsonObject.getString(SIZE));
//        if (!jsonObject.isNull(CERTIFICATION))  {
//            Certification certification = mCertificationMapper.fromJsonString(jsonObject.getString(CERTIFICATION));
//            advert.setCertification(certification);
//        }
//        if (!jsonObject.isNull(CERTIFICATION_ID)) advert.setCertificationId(jsonObject.getInt(CERTIFICATION_ID));
//        advert.setCertificationExtra(jsonObject.getString(CERTIFICATION_EXTRA));
//        if (!jsonObject.isNull(CONDITION)) advert.setConditionId(jsonObject.getInt(CONDITION));
//        if (!jsonObject.isNull(ITEMS_COUNT)) advert.setItemsCount(jsonObject.getInt(ITEMS_COUNT));
//        if (!jsonObject.isNull(PACKAGING_NAME)) advert.setPackagingName(jsonObject.getString(PACKAGING_NAME));
//        advert.setOffersCount(jsonObject.getString(OFFERS_COUNT));
//        if (jsonObject.has(QUESTIONS_COUNT)) advert.setQuestionsCount(jsonObject.getString(QUESTIONS_COUNT));
//        advert.setDaysLeft(jsonObject.getString(DAYS_LEFT));
//
//        JSONArray jsonTagsArray = jsonObject.getJSONArray(TAGS);
//        List<String> tags = new ArrayList<>(jsonTagsArray.length());
//        for (int i = 0; i < jsonTagsArray.length(); i++) {
//            tags.add(jsonTagsArray.getString(i));
//        }
//        advert.setTags(tags);
//
//        User user = mUserMapper.fromJsonString(jsonObject.getString(AUTHOR_DETAILED));
//        advert.setUser(user);
//
//        JSONArray jsonPhotosArray = jsonObject.getJSONArray(PHOTOS);
//        List<Photo> photos = new ArrayList<>(jsonPhotosArray.length());
//        for (int i = 0; i < jsonPhotosArray.length(); i++) {
//            photos.add(mPhotoMapper.fromJsonString(jsonPhotosArray.getString(i)));
//        }
//        advert.setPhotos(photos);
//
//        JSONArray jsonSubscribersArray = jsonObject.getJSONArray(SUBSCRIBERS);
//        List<Integer> subscribers = new ArrayList<>(jsonSubscribersArray.length());
//        for (int i = 0; i < jsonSubscribersArray.length(); i++) {
//            subscribers.add(jsonSubscribersArray.getInt(i));
//        }
//        advert.setSubscribers(subscribers);

//        return advert;
        return null;
    }

    @Override public String toJsonString(Advert target) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NAME, target.getName());
        jsonObject.put(EXPIRES_AT, target.getExpiresAt());
        jsonObject.put(GUIDE_PRICE, target.getGuidePrice());
        jsonObject.put(DESCRIPTION, target.getDescription());
        jsonObject.put(LOCATION, target.getLocation());
        jsonObject.put(SHIPPING, target.getShippingId());
        jsonObject.put(AUTHOR, target.getAuthorId());
        jsonObject.put(CATEGORY, target.getCategoryId());
//        jsonObject.put(SUBCATEGORY, target.getSubCategoryId());
        jsonObject.put(PACKAGING, target.getPackagingId());
        jsonObject.put(MIN_ORDER_QUANTITY, target.getMinOrderQuantity());
        jsonObject.put(SIZE, target.getSize());
        jsonObject.put(CERTIFICATION_ID, target.getCertificationId());
        jsonObject.put(CERTIFICATION_EXTRA, target.getCertificationExtra());
        jsonObject.put(CONDITION, target.getConditionId());
        jsonObject.put(ITEMS_COUNT, target.getItemsCount());
        jsonObject.put(TAGS, new JSONArray(target.getTags()));
        JSONArray jsonArray = new JSONArray();
        for (Photo photo : target.getPhotos()) {
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
