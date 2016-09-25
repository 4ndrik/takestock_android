package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.utils.Encoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Victor Artemyev on 16/09/2016.
 */
public class MakeAdvertJson implements JsonModel {

    public final String name;
    public final String guide_price;
    public final String description;
    public final String location;
    public final int shipping;
    public final int author;
    public final int category;
    public final int subcategory;
    public final int packaging;
    public final int min_order_quantity;
    public final String expires_at;
    public final String size;
    public final int certification_id;
    public final String certification_extra;
    public final int condition;
    public final int items_count;
    public final String[] tags;
    public final String[] photos_list;
    public final boolean in_drafts;

    public MakeAdvertJson(Advert advert) throws IOException {
        this.name = advert.getName();
        this.guide_price = advert.getGuidePrice();
        this.description = advert.getDescription();
        this.location = advert.getLocation();
        this.shipping = advert.getShippingId();
        this.author = advert.getAuthorId();
        this.category = advert.getCategoryId();
        this.subcategory = advert.getSubcategoryId();
        this.packaging = advert.getPackagingId();
        this.min_order_quantity = advert.getMinOrderQuantity();
        this.expires_at = advert.getExpiresAt();
        this.size = advert.getSize();
        this.certification_id = advert.getCertificationId();
        this.certification_extra = advert.getCertificationExtra();
        this.condition = advert.getConditionId();
        this.items_count = advert.getItemsCount();
        this.tags = advert.getTags().toArray(new String[advert.getTags().size()]);
        this.photos_list = toPhotoList(advert.getPhotos());
        this.in_drafts = advert.isInDrafts();
    }

    private String[] toPhotoList(List<Photo> photos) throws IOException {
        String[] result = new String[photos.size()];
        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            result[i] = "data:image/jpg;base64," + Encoder.encodeFileToBase64(photo.getImagePath());
        }
        return result;
    }
}
