package com.prasad.findphoto.dao;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class Photo implements Parcelable {

    public String id;
    public String owner;
    public String secret;
    public String server;
    public  int farm;
    public  String title;
    public int ispublic;
    public int isfriend;
    public int isfamily;
    public int isDownloadStarted =  0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(owner);
        dest.writeString(secret);
        dest.writeString(server);
        dest.writeInt(farm);
        dest.writeString(title);
        dest.writeInt(ispublic);
        dest.writeInt(isfriend);
        dest.writeInt(isfamily);
        dest.writeInt(isDownloadStarted);
    }
}
