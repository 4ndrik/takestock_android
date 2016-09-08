package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Packaging;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/09/2016.
 */
public class PackagingJson {

    private final List<Packaging> packagings;

    public PackagingJson(List<Packaging> packagings) {
        this.packagings = packagings;
    }

    public List<Packaging> getPackagings() {
        return packagings;
    }
}
