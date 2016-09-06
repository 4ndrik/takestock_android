package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Author;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class AuthorJson {

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
    public String bussines_name;
    public String postcode;
    public String vat_number;
    //            "stripe_id": null
//            "last4": null
    public boolean has_notifications;
//            "bussines_type": null
//            "business_sub_type": null

    public Author getAuthor() {
        return new Author.Builder()
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
                .setPostcode(postcode)
                .setVatNumber(vat_number)
                .setHasNotifications(has_notifications)
                .build();
    }

}
