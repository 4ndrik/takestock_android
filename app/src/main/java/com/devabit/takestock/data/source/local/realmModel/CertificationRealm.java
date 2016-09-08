package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Certification;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class CertificationRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mName;
    private String mDescription;
    private String mLogoUrl;

    public CertificationRealm() {}

    public CertificationRealm(Certification certification) {
        mId = certification.getId();
        mName = certification.getName();
        mDescription = certification.getDescription();
        mLogoUrl = certification.getLogoUrl();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        mLogoUrl = logoUrl;
    }

    public Certification getCertification() {
        return new Certification.Builder()
                .setId(mId)
                .setName(mName)
                .setDescription(mDescription)
                .setLogoUrl(mLogoUrl)
                .build();
    }
}
