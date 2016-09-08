package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Certification;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class CertificationListJson implements JsonModel {

    private final List<Certification> certifications;

    public CertificationListJson(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }
}
