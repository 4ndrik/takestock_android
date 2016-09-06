package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.BuildConfig;
import com.devabit.takestock.data.model.Certification;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class CertificationJson implements JsonModel {

    private static final String BASE_URL = BuildConfig.API_URL;

    public int pk;
    public String name;
    public String description;
    public String logo;

    public Certification getCertification() {
        return new Certification.Builder()
                .setId(pk)
                .setName(name)
                .setDescription(description)
                .setLogoUrl(BASE_URL + logo)
                .build();
    }
}
