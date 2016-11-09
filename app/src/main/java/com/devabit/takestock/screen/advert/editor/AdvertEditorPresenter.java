package com.devabit.takestock.screen.advert.editor;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.*;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
class AdvertEditorPresenter implements AdvertEditorContract.Presenter {

    private final DataRepository mDataRepository;
    private final AdvertEditorContract.View mCreateView;

    private CompositeSubscription mSubscriptions;

    AdvertEditorPresenter(@NonNull DataRepository dataRepository, @NonNull AdvertEditorContract.View createView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mCreateView = checkNotNull(createView, "createView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mCreateView.setPresenter(AdvertEditorPresenter.this);
    }

    @Override public void resume() {
    }

    @Override public void fetchAdvertRelatedData() {
        mCreateView.setProgressIndicator(true);
        Subscription subscription = buildAdvertRelatedDataObservable()
                .compose(RxTransformers.<Void>applyObservableSchedulers())
                .subscribe(new Subscriber<Void>() {
                    @Override public void onCompleted() {
                        mCreateView.setProgressIndicator(false);
                        mCreateView.showAdvertRelatedDataFetched();
                    }

                    @Override public void onError(Throwable throwable) {
                        handleError(throwable);
                    }

                    @Override public void onNext(Void v) {

                    }
                });
        mSubscriptions.add(subscription);
    }

    private Observable<Void> buildAdvertRelatedDataObservable() {
        return Observable.defer(
                new Func0<Observable<List<Category>>>() {
                    @Override public Observable<List<Category>> call() {
                        return mDataRepository.getCategories()
                                .compose(RxTransformers.<List<Category>>applyObservableSchedulers())
                                .doOnNext(new Action1<List<Category>>() {
                                    @Override public void call(List<Category> categories) {
                                        mCreateView.showCategoriesInView(categories);
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
                                        mCreateView.showPackagingsInView(packagings);
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
                                        mCreateView.showShippingsInView(shippings);
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
                                        mCreateView.showConditionsInView(conditions);
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
                                        mCreateView.showSizesInView(sizes);
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
                                        mCreateView.showCertificationsInView(certifications);
                                    }
                                });
                    }
                })
                .map(new Func1<List<Certification>, Void>() {
                    @Override public Void call(List<Certification> certifications) {
                        return null;
                    }
                });
    }

    @Override public void saveAdvert(Advert advert) {
        if (!isAdvertValid(advert)) return;
        advert.setInDrafts(true);
        mCreateView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveAdvert(advert)
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {
                        mCreateView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mCreateView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(Advert advert) {
                        mCreateView.showSavedAdvertInView(advert);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void editAdvert(Advert advert) {
        if (!isAdvertValid(advert)) return;
        mCreateView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .editAdvert(advert)
                .compose(RxTransformers.<Advert>applyObservableSchedulers())
                .subscribe(new Subscriber<Advert>() {
                    @Override public void onCompleted() {
                        mCreateView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mCreateView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(Advert advert) {
                        mCreateView.showEditedAdvertInView(advert);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mCreateView.showNetworkConnectionError();
        } else {
            mCreateView.showUnknownError();
        }
    }

    @Override public void previewAdvert(Advert advert) {
        if (!isAdvertValid(advert)) return;
        mCreateView.showPreviewedAdvertInView(advert);
    }

    private boolean isAdvertValid(Advert advert) {
        return validatePhotos(advert)
                && validateName(advert)
                && validateCategory(advert)
                && validateSubcategory(advert)
                && validateItemCount(advert)
                && validateMinimumOrder(advert)
                && validateGuidePrice(advert)
                && validateDescription(advert)
                && validateLocation(advert)
                && validateShipping(advert)
                && validateCondition(advert)
                && validateExpiryDate(advert)
//                && validateSize(advert)
                && validateCertification(advert);
//                && validateCertificationExtra(advert);
    }

    private boolean validateCategory(Advert advert) {
        if (advert.getCategoryId() <= 0) {
            mCreateView.showEmptyCategoryError();
            return false;
        }
        return true;
    }

    private boolean validateSubcategory(Advert advert) {
        if (advert.getSubcategoryId() <= 0) {
            mCreateView.showEmptySubcategoryError();
            return false;
        }
        return true;
    }

    private boolean validateShipping(Advert advert) {
        if (advert.getShippingId() <= 0) {
            mCreateView.showEmptyShippingError();
            return false;
        }
        return true;
    }

    private boolean validateCondition(Advert advert) {
        if (advert.getConditionId() <= 0) {
            mCreateView.showEmptyConditionError();
            return false;
        }
        return true;
    }

    private boolean validatePhotos(Advert advert) {
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            mCreateView.showEmptyPhotosError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateName(Advert advert) {
        String name = advert.getName();
        if (name.isEmpty()) {
            mCreateView.showEmptyTitleError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateItemCount(Advert advert) {
        int itemCount = advert.getItemsCount();
        if (itemCount == 0) {
            mCreateView.showEmptyItemCountError();
            return false;
        }
        return true;
    }

    private boolean validateMinimumOrder(Advert advert) {
        int minOrder = advert.getMinOrderQuantity();
        if (minOrder == 0) {
            mCreateView.showEmptyMinimumOrderError();
            return false;
        }
        return true;
    }

    private boolean validateGuidePrice(Advert advert) {
        String price = advert.getGuidePrice();
        if (price.isEmpty()) {
            mCreateView.showEmptyGuidePriceError();
            return false;
        }
        return true;
    }

    private boolean validateDescription(Advert advert) {
        String description = advert.getDescription();
        if (description.isEmpty()) {
            mCreateView.showEmptyDescriptionError();
            return false;
        }
        return true;
    }

    private boolean validateLocation(Advert advert) {
        String location = advert.getLocation();
        if (location.isEmpty()) {
            mCreateView.showEmptyLocationError();
            return false;
        }
        return true;
    }

    private boolean validateExpiryDate(Advert advert) {
        if (!advert.isFood()) return true;
        String date = advert.getExpiresAt();
        if (date == null || date.isEmpty()) {
            mCreateView.showEmptyExpiryDateError();
            return false;
        }
        return true;
    }

    private boolean validateSize(Advert advert) {
        String size = advert.getSize();
        if (size.isEmpty()) {
            mCreateView.showEmptySizeError();
            return false;
        }
        return true;
    }

    private boolean validateCertification(Advert advert) {
        int certificationId = advert.getCertificationId();
        if (certificationId <= 0) {
            mCreateView.showEmptyCertificationError();
            return false;
        }
        return true;
    }

    private boolean validateCertificationExtra(Advert advert) {
        String text = advert.getCertificationExtra();
        if (text.isEmpty()) {
            mCreateView.showEmptyCertificationExtraError();
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
