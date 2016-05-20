package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Certification;
import com.devabit.takestock.data.source.local.entity.CertificationEntity;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 04/05/2016.
 */
public class CertificationEntityDataMapper {

    public List<CertificationEntity> transformToEntityList(List<Certification> certifications) {
        List<CertificationEntity> result = new ArrayList<>(certifications.size());
        for (Certification certification : certifications) {
            CertificationEntity entity = transformToEntity(certification);
            result.add(entity);
        }
        return result;
    }

    public CertificationEntity transformToEntity(Certification certification) {
        CertificationEntity entity = new CertificationEntity();
        entity.setId(certification.getId());
        entity.setName(certification.getName());
        entity.setDescription(certification.getDescription());
        entity.setLogoUrl(certification.getLogoUrl());
        return entity;
    }

    public List<Certification> transformFromEntityList(RealmResults<CertificationEntity> entities) {
        List<Certification> result = new ArrayList<>(entities.size());
        for (CertificationEntity entity : entities) {
            Certification certification = transformFromEntity(entity);
            result.add(certification);
        }
        return result;
    }

    public Certification transformFromEntity(CertificationEntity entity) {
        if (entity == null) return null;
        Certification certification = new Certification();
        certification.setId(entity.getId());
        certification.setName(entity.getName());
        certification.setDescription(entity.getDescription());
        certification.setLogoUrl(entity.getLogoUrl());
        return certification;
    }
}
