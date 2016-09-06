package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class AdvertJson implements JsonModel {

    public int id;
    public String name;
    public String created_at;
    public String expires_at;
    public String updated_at;
    public String guide_price;
    public String description;
    public String location;
    public int shipping;
    public String shipping_display;
    public boolean is_vat_exempt;
    public PhotoJson[] photos;
    public int author;
    public int category;
    public int subcategory;
    public int packaging;
    public int min_order_quantity;
    public String size;
    public CertificationJson certification;
    public String certification_extra;
    public int certification_id;
    public int condition;
    public String condition_display;
    public int items_count;
    public int items_count_now;
    public String[] tags;
    public AuthorJson author_detailed;
    public String packaging_name;
    public String offers_count;
    public String questions_count;
    public String days_left;
    public int[] subscribers;
    public boolean in_drafts;
    public int advert_views;
    public boolean can_offer;
    public int notifications;
    public int new_q_count;
    public int new_offers_count;
    public boolean is_food;
    public int state;
    public String escaped_description;
    public String category_name;

    public Advert getAdvert() {
        return new Advert.Builder()
                .setId(id)
                .setName(name)
                .setCreatedAt(created_at)
                .setExpiresAt(expires_at)
                .setUpdatedAt(updated_at)
                .setGuidePrice(guide_price)
                .setDescription(description)
                .setLocation(location)
                .setShippingId(shipping)
                .setShippingDisplay(shipping_display)
                .setIsVatExempt(is_vat_exempt)
                .setPhotos(getPhotos())
                .setAuthorId(author)
                .setCategoryId(category)
                .setSubcategoryId(subcategory)
                .setPackagingId(packaging)
                .setMinOrderQuantity(min_order_quantity)
                .setSize(size)
                .setCertification(certification.getCertification())
                .setCertificationExtra(certification_extra)
                .setCertificationId(certification_id)
                .setConditionId(condition)
                .setConditionDisplay(condition_display)
                .setItemsCount(items_count)
                .setItemsCountNow(items_count_now)
                .setTags(Arrays.asList(tags))
                .setAuthor(author_detailed.getAuthor())
                .setPackagingName(packaging_name)
                .setOffersCount(offers_count)
                .setQuestionsCount(questions_count)
                .setDaysLeft(days_left)
                .setSubscribers(subscribers)
                .setInDrafts(in_drafts)
                .setAdvertsViews(advert_views)
                .setCanOffer(can_offer)
                .setNotifications(notifications)
                .setNewQuestionsCount(new_q_count)
                .setNewOffersCount(new_offers_count)
                .setIsFood(is_food)
                .setEscapedDescription(escaped_description)
                .setCategoryName(category_name)
                .build();
    }

    private List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>(this.photos.length);
        for (PhotoJson json : this.photos) {
            photos.add(json.getPhoto());
        }
        return photos;
    }
}
