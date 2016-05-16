package com.devabit.takestock.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Victor Artemyev on 24/02/2016.
 */
public class BitmapUtil {

    public static final int QUALITY_75 = 75;

    public static final int MAX_WIDTH = 480;
    public static final int MAX_HEIGHT = 480;

    public static final int ORIENTATION_ROTATE_90 = ExifInterface.ORIENTATION_ROTATE_90;
    public static final int ORIENTATION_ROTATE_180 = ExifInterface.ORIENTATION_ROTATE_180;

    public static Bitmap rotateBitmapPerOrientation(Bitmap target, Uri uri) throws IOException {

        int orientation = getBitmapOrientation(uri);

        switch (orientation) {
            case ORIENTATION_ROTATE_90:
                return rotateBitmapPerAngle(target, 90);

            case ORIENTATION_ROTATE_180:
                return rotateBitmapPerAngle(target, 180);

            default:
                return target;
        }
    }

    public static int getBitmapOrientation(Uri uri) throws IOException {
        ExifInterface exifInterface = new ExifInterface(uri.getPath());
        return exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
    }

    public static Bitmap rotateBitmapPerAngle(Bitmap target, int angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(target, 0, 0, target.getWidth(), target.getHeight(), matrix, true);
        target.recycle();

        return retVal;
    }

    public static Bitmap getBitmapFromUri(Uri uri) {
        return getBitmapFromUri(uri, MAX_WIDTH, MAX_HEIGHT);
    }

    public static Bitmap getBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static File saveBitmapToFile(Bitmap bitmap, File file) throws IOException {
        return saveBitmapToFile(bitmap, file, QUALITY_75);
    }

    public static File saveBitmapToFile(Bitmap bitmap, File file, int quality) throws IOException {
        return saveBitmapToFile(bitmap, file, quality, Bitmap.CompressFormat.JPEG);
    }

    public static File saveBitmapToFile(Bitmap bitmap, File file, int quality,
                                        Bitmap.CompressFormat format) throws IOException {
        FileOutputStream outStream = new FileOutputStream(file);
        bitmap.compress(format, quality, outStream);
        outStream.flush();
        outStream.close();
        return file;
    }
}
