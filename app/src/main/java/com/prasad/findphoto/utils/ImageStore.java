package com.prasad.findphoto.utils;

import android.graphics.Bitmap;
import java.util.HashMap;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class ImageStore {
    public static ImageStore imageStore;
    private HashMap<String,Bitmap> imgCache =  new HashMap<String, Bitmap>();

    public static ImageStore getInstance(){
        if(imageStore == null) {
            imageStore = new ImageStore();

        }
        return imageStore;
    }
    public void addBitmapToCache(String key, Bitmap bitmap) {
        imgCache.put(key,bitmap);
    }

    public Bitmap getBitmapFromDiskCache(String key) {

        if(imgCache.containsKey(key)){
            return imgCache.get(key);
        }
        return null;
    }

}
