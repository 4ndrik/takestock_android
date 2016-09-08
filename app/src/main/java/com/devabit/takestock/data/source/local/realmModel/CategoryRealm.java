package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.Category;
import com.devabit.takestock.data.model.Subcategory;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class CategoryRealm extends RealmObject {

    private @PrimaryKey int mId;
    private String mName;
    private boolean mIsFood;
    private RealmList<SubcategoryRealm> mSubcategories;

    public CategoryRealm() {}

    public CategoryRealm(Category category) {
        mId = category.getId();
        mName = category.getName();
        mIsFood = category.isFood();
        mSubcategories = toRealmList(category.getSubcategories());
    }

    private RealmList<SubcategoryRealm> toRealmList(List<Subcategory> subcategories) {
        RealmList<SubcategoryRealm> list = new RealmList<>();
        for (Subcategory subcategory : subcategories) {
            list.add(new SubcategoryRealm(subcategory));
        }
        return list;
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

    public Category getCategory() {
        return new Category.Builder()
                .setId(mId)
                .setName(mName)
                .setIsFood(mIsFood)
                .setSubcategories(toSubcategoryList(mSubcategories))
                .build();
    }

    private List<Subcategory> toSubcategoryList(RealmList<SubcategoryRealm> realmList) {
        List<Subcategory> list = new ArrayList<>(realmList.size());
        for (SubcategoryRealm subcategoryRealm : realmList) {
            list.add(subcategoryRealm.getSubcategory());
        }
        return list;
     }
}
