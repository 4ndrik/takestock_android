package com.devabit.takestock.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static com.devabit.takestock.utils.Logger.LOGD;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 09/06/2016.
 */
public final class FileUtil {

    private static final String TAG = makeLogTag(FileUtil.class);

    public static File getPhotoFile() {
        File publicPictureDirection = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(publicPictureDirection, "TakeStock");
        if (!mediaStorageDir.exists()) {
            LOGD(TAG, mediaStorageDir.getPath() + " created: " + mediaStorageDir.mkdirs());
        }
        return new File(
                mediaStorageDir.getPath() + File.separator
                        + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".jpg");
    }

    public static File getUniquePhotoFile(Context context) {
        return new File(context.getCacheDir().getPath() + File.separator + UUID.randomUUID().toString() + ".jpg");
    }
}
