package com.devabit.takestock.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Victor Artemyev on 23/02/2016.
 */
public class Encoder {

    public static String encodeFileToBase64(String filePath) throws IOException {
        if (filePath.startsWith("file:")) {
            filePath = filePath.substring(5);
        }

        InputStream input = new FileInputStream(filePath);
        return encodeToBase64(input);
    }

    public static String encodeUrlToBase64(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        InputStream input = conn.getInputStream();
        return encodeToBase64(input);
    }

    private static String encodeToBase64(InputStream inputStream) throws IOException {
        byte[] bytes;
        byte[] buffer = new byte[2048];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        bytes = output.toByteArray();
        output.flush();
        inputStream.close();
        output.close();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] encodedByte = baos.toByteArray();
        return Base64.encodeToString(encodedByte, Base64.NO_WRAP);
    }
}
