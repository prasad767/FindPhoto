package com.prasad.findphoto.callback;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public interface GalleryClickListener {

    public void photoSelected(String url, String title);
    public void insert(String from, String to);
}
