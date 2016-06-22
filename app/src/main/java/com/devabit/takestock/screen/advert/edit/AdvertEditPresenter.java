package com.devabit.takestock.screen.advert.edit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.utils.BitmapUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class AdvertEditPresenter implements AdvertEditContract.Presenter {

    private static final String TAG = makeLogTag(AdvertEditPresenter.class);

    private final DataRepository mDataRepository;
    private final AdvertEditContract.View mEditView;

    private CompositeSubscription mSubscriptions;

    private boolean mIsAdvertRelatedDataShowed;

    public AdvertEditPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertEditContract.View editView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mEditView = checkNotNull(editView, "editView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mEditView.setPresenter(AdvertEditPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void fetchAdvertRelatedData() {
        if (mIsAdvertRelatedDataShowed) return;
        mEditView.setProgressIndicator(true);
        Subscription subscription = buildAdvertRelatedDataObservable()
                .compose(RxTransformers.<List<Certification>>applyObservableSchedulers())
                .subscribe(new Subscriber<List<Certification>>() {
                    @Override public void onCompleted() {
                        mIsAdvertRelatedDataShowed = true;
                        mEditView.onAdvertRelatedDataShowed();
                        mEditView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mEditView.setProgressIndicator(false);
                        mEditView.showUnknownError();
                    }

                    @Override public void onNext(List<Certification> certifications) {

                    }
                });
        mSubscriptions.add(subscription);
    }

    private Observable<List<Certification>> buildAdvertRelatedDataObservable() {
        return Observable.defer(
                new Func0<Observable<List<Category>>>() {
                    @Override public Observable<List<Category>> call() {
                        return mDataRepository.getCategories()
                                .compose(RxTransformers.<List<Category>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Category>>() {
                                    @Override public void call(List<Category> categories) {
                                        mEditView.showCategoriesInView(categories);
                                    }
                                });
                    }
                })
                .flatMap(new Func1<List<Category>, Observable<List<Packaging>>>() {
                    @Override public Observable<List<Packaging>> call(List<Category> categories) {
                        return mDataRepository.getPackagings()
                                .compose(RxTransformers.<List<Packaging>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Packaging>>() {
                                    @Override public void call(List<Packaging> packagings) {
                                        mEditView.showPackagingsInView(packagings);
                                    }
                                });
                    }
                })
                .flatMap(new Func1<List<Packaging>, Observable<List<Shipping>>>() {
                    @Override public Observable<List<Shipping>> call(List<Packaging> packagings) {
                        return mDataRepository
                                .getShippings()
                                .compose(RxTransformers.<List<Shipping>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Shipping>>() {
                                    @Override public void call(List<Shipping> shippings) {
                                        mEditView.showShippingsInView(shippings);
                                    }
                                });
                    }
                })
                .flatMap(new Func1<List<Shipping>, Observable<List<Condition>>>() {
                    @Override public Observable<List<Condition>> call(List<Shipping> shippings) {
                        return mDataRepository.getConditions()
                                .compose(RxTransformers.<List<Condition>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Condition>>() {
                                    @Override public void call(List<Condition> conditions) {
                                        mEditView.showConditionsInView(conditions);
                                    }
                                });
                    }
                })
                .flatMap(new Func1<List<Condition>, Observable<List<Size>>>() {
                    @Override public Observable<List<Size>> call(List<Condition> conditions) {
                        return mDataRepository.getSizes()
                                .compose(RxTransformers.<List<Size>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Size>>() {
                                    @Override public void call(List<Size> sizes) {
                                        mEditView.showSizesInView(sizes);
                                    }
                                });
                    }
                })
                .flatMap(new Func1<List<Size>, Observable<List<Certification>>>() {
                    @Override public Observable<List<Certification>> call(List<Size> sizes) {
                        return mDataRepository.getCertifications()
                                .compose(RxTransformers.<List<Certification>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Certification>>() {
                                    @Override public void call(List<Certification> certifications) {
                                        mEditView.showCertificationsInView(certifications);
                                    }
                                });
                    }
                });

    }

    @Override public void processPhotoUriToFile(Uri photoUri, final File photoFile) {
        mEditView.setProgressIndicator(true);
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
                        mEditView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mEditView.setProgressIndicator(false);
                        mEditView.showUnknownError();
                    }

                    @Override public void onNext(Photo photo) {
                        mEditView.showPhotoInView(photo);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void previewAdvert(Advert advert) {
//        if(!isAdvertDataValid(advert)) return;
//        mEditView.showAdvertInPreview(advert);
    }

    @Override public void updateAdvert(Advert advert) {
        if (!isAdvertDataValid(advert)) return;
        mEditView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveAdvert(advert)
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {
                        mEditView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mEditView.setProgressIndicator(false);
                    }

                    @Override public void onNext(Advert advert) {
                        mEditView.showAdvertUpdated(advert);
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
            mEditView.showEmptyPhotosError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateName(Advert advert) {
        String name = advert.getName();
        if (name.isEmpty()) {
            mEditView.showEmptyTitleError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateItemCount(Advert advert) {
        int itemCount = advert.getItemsCount();
        if (itemCount == 0) {
            mEditView.showEmptyItemCountError();
            return false;
        }
        return true;
    }

    private boolean validateMinimumOrder(Advert advert) {
        int minOrder = advert.getMinOrderQuantity();
        if (minOrder == 0) {
            mEditView.showEmptyMinimumOrderError();
            return false;
        }
        return true;
    }

    private boolean validateGuidePrice(Advert advert) {
        String price = advert.getGuidePrice();
        if (price.isEmpty()) {
            mEditView.showEmptyGuidePriceError();
            return false;
        }
        return true;
    }

    private boolean validateDescription(Advert advert) {
        String description = advert.getDescription();
        if (description.isEmpty()) {
            mEditView.showEmptyDescriptionError();
            return false;
        }
        return true;
    }

    private boolean validateLocation(Advert advert) {
        String location = advert.getLocation();
        if (location.isEmpty()) {
            mEditView.showEmptyLocationError();
            return false;
        }
        return true;
    }

    private boolean validateExpiryDate(Advert advert) {
        String date = advert.getDateExpiresAt();
        if (date.isEmpty()) {
            mEditView.showEmptyExpiryDateError();
            return false;
        }
        return true;
    }

    private boolean validateSize(Advert advert) {
        String size = advert.getSize();
        if (size.isEmpty()) {
            mEditView.showEmptySizeError();
            return false;
        }
        return true;
    }

    private boolean validateCertification(Advert advert) {
        int certificationId = advert.getCertificationId();
        if (certificationId == 0) {
            mEditView.showEmptyCertificationError();
            return false;
        }
        return true;
    }

    private boolean validateCertificationExtra(Advert advert) {
        String text = advert.getCertificationExtra();
        if (text.isEmpty()) {
            mEditView.showEmptyCertificationExtraError();
            return false;
        }
        return true;
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {
    }
}
