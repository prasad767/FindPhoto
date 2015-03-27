package com.prasad.findphoto.callback;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public interface ResponseCallback {


    public void onSuccess(String json);
    public void onFailure(String json);
}
