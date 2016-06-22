package com.devabit.takestock.screen.profile.edit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.User;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.utils.BitmapUtil;
import com.devabit.takestock.utils.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.io.File;
import java.io.IOException;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 07/06/2016.
 */
public class ProfileEditPresenter implements ProfileEditContract.Presenter {

    private static final String TAG = Logger.makeLogTag(ProfileEditPresenter.class);

    private final DataRepository mDataRepository;
    private final ProfileEditContract.View mProfileView;

    private CompositeSubscription mSubscriptions;

    public ProfileEditPresenter(@NonNull DataRepository dataRepository, @NonNull ProfileEditContract.View profileView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mProfileView = checkNotNull(profileView, "profileView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mProfileView.setPresenter(ProfileEditPresenter.this);
    }


    @Override public void resume() {

    }

    @Override public void processPhotoUriToFile(Uri photoUri, final File uniqueFile) {
        mProfileView.setProgressIndicator(true);
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
                }).map(new Func1<Bitmap, String>() {
                    @Override public String call(Bitmap bitmap) {
                        try {
                            File file = BitmapUtil.saveBitmapToFile(bitmap, uniqueFile);
                            return "file:" + file.getAbsolutePath();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .compose(RxTransformers.<String>applyObservableSchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override public void onCompleted() {
                        mProfileView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mProfileView.setProgressIndicator(false);
                        mProfileView.showPhotoError();
                    }

                    @Override public void onNext(String path) {
                        mProfileView.showPhotoInView(path);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override public void updateUser(User user) {

    }

    @NonNull private Action1<Throwable> getOnError() {
        return new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                mProfileView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", throwable);
                if (throwable instanceof NetworkConnectionException) {
                    mProfileView.showNetworkConnectionError();
                } else {
                    mProfileView.showUnknownError();
                }
            }
        };
    }

    @NonNull private Action0 getOnCompleted() {
        return new Action0() {
            @Override public void call() {
                mProfileView.setProgressIndicator(false);
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
