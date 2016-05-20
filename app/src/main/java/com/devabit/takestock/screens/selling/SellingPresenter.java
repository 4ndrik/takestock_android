package com.devabit.takestock.screens.selling;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.util.BitmapUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class SellingPresenter implements SellingContract.Presenter {

    private static final String TAG = makeLogTag(SellingPresenter.class);

    private final DataRepository mDataRepository;
    private final SellingContract.View mSellingView;

    private CompositeSubscription mSubscriptions;

    public SellingPresenter(@NonNull DataRepository dataRepository, @NonNull SellingContract.View sellingView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSellingView = checkNotNull(sellingView, "sellingView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSellingView.setPresenter(SellingPresenter.this);
    }

    @Override public void create() {
        setUpCategoriesToView();
        setUpPackagingToView();
        setUpShippingToView();
        setUpConditionsToView();
        setUpSizesToView();
        setUpCertificationsToView();
    }

    private void setUpCategoriesToView() {
        Subscription subscription = mDataRepository
                .getCategories()
                .compose(RxTransformers.<List<Category>>applyObservableSchedulers())
                .subscribe(new Action1<List<Category>>() {
                    @Override public void call(List<Category> categories) {
                        mSellingView.showCategoriesInView(categories);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpPackagingToView() {
        Subscription subscription = mDataRepository
                .getPackagings()
                .compose(RxTransformers.<List<Packaging>>applyObservableSchedulers())
                .subscribe(new Action1<List<Packaging>>() {
                    @Override public void call(List<Packaging> packagings) {
                        mSellingView.showPackagingsInView(packagings);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpShippingToView() {
        Subscription subscription = mDataRepository
                .getShippings()
                .compose(RxTransformers.<List<Shipping>>applyObservableSchedulers())
                .subscribe(new Action1<List<Shipping>>() {
                    @Override public void call(List<Shipping> shippings) {
                        mSellingView.showShippingsInView(shippings);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpConditionsToView() {
        Subscription subscription = mDataRepository
                .getConditions()
                .compose(RxTransformers.<List<Condition>>applyObservableSchedulers())
                .subscribe(new Action1<List<Condition>>() {
                    @Override public void call(List<Condition> conditions) {
                        mSellingView.showConditionsInView(conditions);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpSizesToView() {
        Subscription subscription = mDataRepository
                .getSizes()
                .compose(RxTransformers.<List<Size>>applyObservableSchedulers())
                .subscribe(new Action1<List<Size>>() {
                    @Override public void call(List<Size> sizes) {
                        mSellingView.showSizesInView(sizes);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void setUpCertificationsToView() {
        Subscription subscription = mDataRepository
                .getCertifications()
                .compose(RxTransformers.<List<Certification>>applyObservableSchedulers())
                .subscribe(new Action1<List<Certification>>() {
                    @Override public void call(List<Certification> certifications) {
                        mSellingView.showCertificationsInView(certifications);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        LOGE(TAG, "BOOM:", throwable);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void resume() {
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }

    @Override public void processPhotoToFile(Uri photoUri, final File photoFile) {
        mSellingView.setProgressIndicator(true);
        Subscription subscription = Observable.just(photoUri)
                .map(new Func1<Uri, Bitmap>() {
                    @Override
                    public Bitmap call(Uri uri) {
                        try {
                            Bitmap bitmap = BitmapUtil.getBitmapFromUri(uri);
                            return BitmapUtil.rotateBitmapPerOrientation(bitmap, uri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).map(new Func1<Bitmap, Photo>() {
                    @Override public Photo call(Bitmap bitmap) {
                        try {
                            File file = BitmapUtil.saveBitmapToFile(bitmap, photoFile);
                            Photo photo = new Photo();
                            photo.setImagePath("file:" + file.getAbsolutePath());
                            return photo;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .compose(RxTransformers.<Photo>applyObservableSchedulers())
                .subscribe(new Subscriber<Photo>() {
                    @Override public void onCompleted() {
                        mSellingView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mSellingView.setProgressIndicator(false);
                        mSellingView.showUnknownError();
                    }

                    @Override public void onNext(Photo photo) {
                        mSellingView.showPhotoInView(photo);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void processAdvert(Advert advert) {
        if(!isAdvertDataValid(advert)) return;
        mSellingView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveAdvert(advert)
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {
                        mSellingView.setProgressIndicator(false);
                        mSellingView.showAdvertCreated();
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mSellingView.setProgressIndicator(false);
                    }

                    @Override public void onNext(Advert advert) {

                    }
                });
        mSubscriptions.add(subscription);
    }

    private boolean isAdvertDataValid(Advert advert) {
        return validatePhotos(advert)
                && validateName(advert)
                && validateItemCount(advert)
                && validateMinimumOrder(advert)
                && validateGuidePrice(advert)
                && validateDescription(advert)
                && validateLocation(advert)
                && validateExpiryDate(advert)
                && validateSize(advert)
                && validateCertification(advert)
                && validateCertificationExtra(advert);
    }

    private boolean validatePhotos(Advert advert) {
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            mSellingView.showEmptyPhotosError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateName(Advert advert) {
        String name = advert.getName();
        if (name.isEmpty()) {
            mSellingView.showEmptyTitleError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateItemCount(Advert advert) {
        int itemCount = advert.getItemsCount();
        if (itemCount == 0) {
            mSellingView.showEmptyItemCountError();
            return false;
        }
        return true;
    }

    private boolean validateMinimumOrder(Advert advert) {
        int minOrder = advert.getMinOrderQuantity();
        if (minOrder == 0) {
            mSellingView.showEmptyMinimumOrderError();
            return false;
        }
        return true;
    }

    private boolean validateGuidePrice(Advert advert) {
        String price = advert.getGuidePrice();
        if (price.isEmpty()) {
            mSellingView.showEmptyGuidePriceError();
            return false;
        }
        return true;
    }

    private boolean validateDescription(Advert advert) {
        String description = advert.getDescription();
        if (description.isEmpty()) {
            mSellingView.showEmptyDescriptionError();
            return false;
        }
        return true;
    }

    private boolean validateLocation(Advert advert) {
        String location = advert.getLocation();
        if (location.isEmpty()) {
            mSellingView.showEmptyLocationError();
            return false;
        }
        return true;
    }

    private boolean validateExpiryDate(Advert advert) {
        String date = advert.getDateExpiresAt();
        if(date.isEmpty()) {
            mSellingView.showEmptyExpiryDateError();
            return false;
        }
        return true;
    }

    private boolean validateSize(Advert advert) {
        String size = advert.getSize();
        if (size.isEmpty()) {
            mSellingView.showEmptySizeError();
            return false;
        }
        return true;
    }

    private boolean validateCertification(Advert advert) {
        int certificationId = advert.getCertificationId();
        if(certificationId == 0) {
            mSellingView.showEmptyCertificationError();
            return false;
        }
        return true;
    }

    private boolean validateCertificationExtra(Advert advert) {
        String text = advert.getCertificationExtra();
        if (text.isEmpty()) {
            mSellingView.showEmptyCertificationExtraError();
            return false;
        }
        return true;
    }
}
