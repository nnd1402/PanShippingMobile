package com.example.panshippingandroid.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static Bitmap uriToBitmap(String imageURIString) {
        Uri imageURI = Uri.parse(imageURIString);
        try {
            return MediaStore.Images.Media.getBitmap(ApplicationContextProvider.getContext().getContentResolver(), imageURI);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static Bitmap convertStringImageToBitmap(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String convertBitmapToStringImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getResizedBitmap(Bitmap source, int mSize) {
        float scale;
        int newSize;
        boolean isHeightScale = false;
        if (source.getHeight() >= source.getWidth()) {
            isHeightScale = true;
        }
        Bitmap scaleBitmap;
        if (isHeightScale) {
            scale = (float) mSize / source.getHeight();
            newSize = Math.round(source.getWidth() * scale);
            scaleBitmap = Bitmap.createScaledBitmap(source, newSize, mSize, true);
        } else {
            scale = (float) mSize / source.getWidth();
            newSize = Math.round(source.getHeight() * scale);
            scaleBitmap = Bitmap.createScaledBitmap(source, mSize, newSize, true);
        }
        return scaleBitmap;
    }
}