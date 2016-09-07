package com.devabit.takestock.data.source.local.dao;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.source.local.realmModel.CategoryRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 07/09/2016.
 */
public class CategoryRealmDao extends AbstractDao {

    private final RealmConfiguration mRealmConfiguration;

    public CategoryRealmDao(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
    }

    public void storeOrUpdateCategoryList(final List<Category> categoryList) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(fromModelToRealm(categoryList));
            }
        });
        realm.close();
    }

    private List<CategoryRealm> fromModelToRealm(List<Category> categoryList) {
        List<CategoryRealm> result = new ArrayList<>(categoryList.size());
        for (Category category : categoryList) {
            result.add(new CategoryRealm(category));
        }
        return result;
    }

    public List<Category> getCategoryList() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        RealmResults<CategoryRealm> results = realm.where(CategoryRealm.class).findAll();
        return fromRealmToModel(results);
    }

    private List<Category> fromRealmToModel(List<CategoryRealm> results) {
        List<Category> categoryList = new ArrayList<>(results.size());
        for (CategoryRealm categoryRealm : results) {
            categoryList.add(categoryRealm.getCategory());
        }
        return categoryList;
    }

    public Category getCategoryWithId(int id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        CategoryRealm categoryRealm = realm
                .where(CategoryRealm.class)
                .equalTo("mId", id)
                .findFirst();
        return categoryRealm.getCategory();
    }

    @Override public void clearDatabase() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.delete(CategoryRealm.class);
            }
        });
        realm.close();
    }
}
