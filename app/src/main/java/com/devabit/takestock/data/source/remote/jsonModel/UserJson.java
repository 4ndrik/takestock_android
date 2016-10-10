package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.source.remote.ApiRest;
import com.devabit.takestock.utils.Encoder;

import java.io.IOException;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class UserJson {

    public int id;
    public String business_sub_type_name;
    public String business_type_name;
    public String last_login;
    public boolean is_superuser;
    public int role;
    public String partnerparent;
    public String partner;
    public String remember_token;
    public String payment_method;
    public String account_number;
    public String service;
    public String group_reference;
    public String group_code;
    public boolean is_seller;
    public String description;
    public String old_id;
    public String username;
    public String first_name;
    public String last_name;
    public String email;
    public boolean is_staff;
    public boolean is_active;
    public String date_joined;
    public boolean is_subscribed;
    public boolean is_verified;
    public boolean is_vat_exempt;
    public double avg_rating;
    public String photo;
    public String photo_b64;
    public String bussines_name;
    public String postcode;
    public String vat_number;
    public int bussines_type;
    public int business_sub_type;
    public String stripe_id;
    public int last4;
    public boolean has_notifications;

    public UserJson(User user) throws IOException {
        this.id = user.getId();
        this.business_sub_type_name = user.getBusinessTypeName();
        this.business_type_name = user.getBusinessTypeName();
        this.last_login = user.getLastLogin();
        this.is_superuser = user.isSuperuser();
        this.role = user.getRole();
        this.partnerparent = user.getPartnerParent();
        this.partner = user.getPartner();
        this.remember_token = user.getRememberToken();
        this.payment_method = user.getPaymentMethod();
        this.account_number = user.getAccountNumber();
        this.service = user.getService();
        this.group_reference = user.getGroupReferences();
        this.group_code = user.getGroupCode();
        this.is_seller = user.isSeller();
        this.description = user.getDescription();
        this.old_id = user.getOldId();
        this.username = user.getUserName();
        this.first_name = user.getFirstName();
        this.last_name = user.getLastName();
        this.email = user.getEmail();
        this.is_staff = user.isStaff();
        this.is_active = user.isActive();
        this.date_joined = user.getDateJoined();
        this.is_subscribed = user.isSubscribed();
        this.is_verified = user.isVerified();
        this.is_vat_exempt = user.isVatExempt();
        this.avg_rating = user.getAvgRating();
        this.photo_b64 = user.getPhoto() == null
                ? null
                : Encoder.encodeFileToBase64(user.getPhoto());
        this.bussines_name = user.getBusinessName();
        this.postcode = user.getPostcode();
        this.vat_number = user.getVatNumber();
        this.bussines_type = user.getBusinessTypeId();
        this.business_sub_type = user.getBusinessSubtypeId();
        this.stripe_id = "";
        this.last4 = 0;
        this.has_notifications = user.hasNotifications();
    }

    public User toUser() {
        return new User.Builder()
                .setId(id)
                .setBusinessName(business_type_name)
                .setBusinessSubtypeName(business_sub_type_name)
                .setLastLogin(last_login)
                .setIsSuperuser(is_superuser)
                .setRole(role)
                .setPartnerParent(partnerparent)
                .setPartner(partner)
                .setRememberToken(remember_token)
                .setPaymentMethod(payment_method)
                .setAccountNumber(account_number)
                .setService(service)
                .setGroupReferences(group_reference)
                .setGroupCode(group_code)
                .setIsSeller(is_seller)
                .setOldId(old_id)
                .setUserName(username)
                .setFirstName(first_name)
                .setLastName(last_name)
                .setEmail(email)
                .setIsStaff(is_staff)
                .setIsActive(is_active)
                .setDateJoined(date_joined)
                .setIsSubscribed(is_subscribed)
                .setIsVerified(is_verified)
                .setIsVatExempt(is_vat_exempt)
                .setAvgRating(avg_rating)
                .setBusinessName(bussines_name)
                .setPhoto(toPhoto())
                .setPostcode(postcode)
                .setVatNumber(vat_number)
                .setHasNotifications(has_notifications)
                .setBusinessTypeId(bussines_type)
                .setBusinessSubtypeId(business_sub_type)
                .build();
    }

    private String toPhoto() {
        if (photo == null) return "";
        return photo.startsWith("http") ? photo : ApiRest.BASE_URL + photo;
    }
}
