package com.devabit.takestock.data.source.local;

import android.content.Context;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataSource;
import com.devabit.takestock.data.source.local.entity.*;
import com.devabit.takestock.data.source.local.mapper.*;
import io.realm.*;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;
import java.util.concurrent.Callable;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 28/04/2016.
 */
public class LocalDataSource implements DataSource {

    private static final String TAG = makeLogTag(LocalDataSource.class);

    private static LocalDataSource sInstance;

    public static LocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocalDataSource(context);
        }
        return sInstance;
    }

    private static final String DATA_BASE_NAME = "takestock.realm";

//    private final Realm mRealm;

    private LocalDataSource(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(DATA_BASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignUp(UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public Observable<AuthToken> obtainAuthTokenPerSignIn(UserCredentials credentials) {
        // Not required because the {@link RemoteDataSource} handles the logic of obtaining the
        // AccessToken from server.
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public void saveCategories(List<Category> categories) {
        CategoryEntityDataMapper categoryMapper = new CategoryEntityDataMapper();
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            for (Category category : categories) {
                CategoryEntity categoryEntity = categoryMapper.transformToEntity(category);
                categoryEntity = realm.copyToRealmOrUpdate(categoryEntity);
                RealmList<SubcategoryEntity> subcategoryEntities = categoryEntity.getSubcategories();
                for (Subcategory subcategory : category.getSubcategories()) {
                    SubcategoryEntity subcategoryEntity = categoryMapper.transformSubcategoryToEntity(subcategory);
                    subcategoryEntities.add(subcategoryEntity);
                }
            }
            realm.commitTransaction();
        }
    }

    @Override public Observable<List<Category>> getCategories() {
        return Observable.fromCallable(new Callable<RealmResults<CategoryEntity>>() {
            @Override public RealmResults<CategoryEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(CategoryEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<CategoryEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<CategoryEntity> categories) {
                return !categories.isEmpty();
            }
        }).map(new Func1<RealmResults<CategoryEntity>, List<Category>>() {
            @Override public List<Category> call(RealmResults<CategoryEntity> entities) {
                return new CategoryEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Category>>() {
            @Override public void call(List<Category> categories) {
                LOGD(TAG, "Categories from LocalDataSource " + categories);
            }
        });
    }

    @Override public Observable<String> getAdverts() {
        return null;
    }

    @Override public void saveSizes(List<Size> sizes) {
        List<SizeEntity> entities = new SizeEntityDataMapper().transformToEntityList(sizes);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Size>> getSizes() {
        return Observable.fromCallable(new Callable<RealmResults<SizeEntity>>() {
            @Override public RealmResults<SizeEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(SizeEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<SizeEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<SizeEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<SizeEntity>, List<Size>>() {
            @Override public List<Size> call(RealmResults<SizeEntity> entities) {
                return new SizeEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Size>>() {
            @Override public void call(List<Size> sizes) {
                LOGD(TAG, "Sizes from LocalDataSource " + sizes);
            }
        });
    }

    @Override public void saveCertifications(List<Certification> certifications) {
        List<CertificationEntity> entities = new CertificationEntityDataMapper()
                .transformToEntityList(certifications);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Certification>> getCertifications() {
        return Observable.fromCallable(new Callable<RealmResults<CertificationEntity>>() {
            @Override public RealmResults<CertificationEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(CertificationEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<CertificationEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<CertificationEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<CertificationEntity>, List<Certification>>() {
            @Override public List<Certification> call(RealmResults<CertificationEntity> entities) {
                return new CertificationEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Certification>>() {
            @Override public void call(List<Certification> certifications) {
                LOGD(TAG, "Certifications from LocalDataSource " + certifications);
            }
        });
    }

    @Override public void saveShippings(List<Shipping> shippings) {
        List<ShippingEntity> entities = new ShippingEntityDataMapper().transformToEntityList(shippings);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Shipping>> getShippings() {
        return Observable.fromCallable(new Callable<RealmResults<ShippingEntity>>() {
            @Override public RealmResults<ShippingEntity> call() throws Exception {
                    return Realm.getDefaultInstance().where(ShippingEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<ShippingEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<ShippingEntity> entities) {
                return !entities.isEmpty();
            }
        }).map(new Func1<RealmResults<ShippingEntity>, List<Shipping>>() {
            @Override public List<Shipping> call(RealmResults<ShippingEntity> entities) {
                return new ShippingEntityDataMapper().transformFromEntityList(entities);
            }
        }).doOnNext(new Action1<List<Shipping>>() {
            @Override public void call(List<Shipping> shippings) {
                LOGD(TAG, "Shippings from LocalDataSource " + shippings);
            }
        });
    }

    @Override public void saveConditions(List<Condition> conditionList) {
        List<ConditionEntity> entities = new ConditionEntityDataMapper()
                .transformToEntityList(conditionList);
        saveOrUpdateEntities(entities);
    }

    @Override public Observable<List<Condition>> getConditions() {
        return Observable.fromCallable(new Callable<RealmResults<ConditionEntity>>() {
            @Override public RealmResults<ConditionEntity> call() throws Exception {
                return Realm.getDefaultInstance().where(ConditionEntity.class).findAll();
            }
        }).filter(new Func1<RealmResults<ConditionEntity>, Boolean>() {
            @Override public Boolean call(RealmResults<ConditionEntity> conditionEntities) {
                return !conditionEntities.isEmpty();
            }
        }).map(new Func1<RealmResults<ConditionEntity>, List<Condition>>() {
            @Override public List<Condition> call(RealmResults<ConditionEntity> conditionEntities) {
                return new ConditionEntityDataMapper().transformFromEntityList(conditionEntities);
            }
        }).doOnNext(new Action1<List<Condition>>() {
            @Override public void call(List<Condition> conditionList) {
                LOGD(TAG, "Conditions from LocalDataSource " + conditionList);
            }
        });
    }

    private <E extends RealmModel> List<E> saveOrUpdateEntities(Iterable<E> objects) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            List<E> result = realm.copyToRealmOrUpdate(objects);
            realm.commitTransaction();
            return result;
        }
    }

    private <E extends RealmModel> E saveOrUpdateEntity(E object) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            E result = realm.copyToRealmOrUpdate(object);
            realm.commitTransaction();
            return result;
        }
    }

}
