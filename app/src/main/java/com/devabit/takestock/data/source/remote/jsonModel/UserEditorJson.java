package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.User;
import com.devabit.takestock.utils.Encoder;

import java.io.IOException;

/**
 * Created by Victor Artemyev on 14/10/2016.
 */

public class UserEditorJson implements JsonModel {

    public int id;
    public String username;
    public String email;
    public boolean is_subscribed;
    public String photo_b64;
    public String bussines_name;
    public String postcode;
    public int bussines_type;
    public int business_sub_type;
    public String vat_number;

    public UserEditorJson(User user) throws IOException {
        this.id = user.getId();
        this.username = user.getUserName();
        this.email = user.getEmail();
        this.is_subscribed = user.isSubscribed();
        this.photo_b64 = user.getPhoto() == null ? null : Encoder.encodeFileToBase64(user.getPhoto());
        this.bussines_name = user.getBusinessName();
        this.postcode = user.getPostcode();
        this.bussines_type = user.getBusinessTypeId();
        this.business_sub_type = user.getBusinessSubtypeId();
        this.vat_number = user.getVatNumber();
    }
}
