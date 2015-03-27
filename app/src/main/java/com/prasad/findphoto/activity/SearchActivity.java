package com.prasad.findphoto.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prasad.findphoto.R;
import com.prasad.findphoto.adapter.PhotoGalleryAdapter;
import com.prasad.findphoto.callback.GalleryClickListener;
import com.prasad.findphoto.callback.ImageRefresh;
import com.prasad.findphoto.callback.ResponseCallback;
import com.prasad.findphoto.dao.Photo;
import com.prasad.findphoto.dao.PublicPhoto;
import com.prasad.findphoto.network.SearchPhoto;
import com.prasad.findphoto.utils.ImageStore;

import java.util.ArrayList;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class SearchActivity extends BaseActivity implements ResponseCallback, ImageRefresh, GalleryClickListener {
    PhotoGalleryAdapter adapter;
    EditText searchTxt;
    Button searchBtn;
    ArrayList<Photo> photos;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTxt = (EditText)findViewById(R.id.search_txt);
       // searchTxt.setText("blue sea");
        //searchTxt.setText("king");
        searchBtn = (Button)findViewById(R.id.search_button);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, calculateSpan(320)));
        adapter = new PhotoGalleryAdapter(photos, R.layout.view_gallery_image);
        adapter.setGalleryClickListener(this);
        recyclerView.setAdapter(adapter);

        if(photos == null || photos.size() ==0 ){
            findViewById(R.id.text_no_photo).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.text_no_photo).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void searchAction(View v){

        if(searchTxt.getText().toString().length() >0) {
            if(photos != null)
            photos.clear();

            ImageStore.getInstance().clean();
            searchBtn.setEnabled(false);

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), 0);
            SearchPhoto c = new SearchPhoto(this);
            c.execute(searchTxt.getText().toString());
        }else{
            Toast.makeText(this,"Enter something to search", Toast.LENGTH_SHORT).show();
         }
    }


    @Override
    public void onSuccess(String json) {
        // showDialog("Downloaded " + result + " bytes");

        // remove unwanted char from response
        json  = json.substring("jsonFlickrApi(".length());
        json =  json.substring(0, json.length()-1);
        Gson gson = new Gson();
        PublicPhoto publicPhoto = gson.fromJson(json, PublicPhoto.class);

        photos = publicPhoto.photos.photo;
        if(photos== null || photos.size() ==0 ){
            findViewById(R.id.text_no_photo).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.text_no_photo).setVisibility(View.INVISIBLE);
        }

        adapter.update(photos);
        searchBtn.setEnabled(true);

    }

    @Override
    public void onFailure(String json) {
        searchBtn.setEnabled(true);
        Toast.makeText(this,"Error in search image", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        //adapter.notifyDataSetChanged();
    }


    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, calculateSpan(320)));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, calculateSpan(320)));
        }
    }

    @Override
    public void photoSelected(String url, String title) {
        Intent detail =  new Intent(this, DetailActivity.class);
        detail.putExtra("URL",url);
        detail.putExtra("TITLE",title);
        startActivity(detail);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.v("onSaveInstanceState", "started");
        outState.putParcelableArrayList("photo", photos);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v("onRestoreInstanceState", "started");
        super.onRestoreInstanceState(savedInstanceState);
        photos = savedInstanceState.getParcelableArrayList("photo");
    }


    public  int calculateSpan( final float px) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return Math.round(width/px);
    }

    @Override
    public void insert(String from, String to) {
        int fromIndex =0;
        int toIndex = 0;
        if(from.equals(to)){
            return;
        }
        for(int i=0;i<photos.size();i++){
            if(photos.get(i).id.equals(from)){
                fromIndex =i;
            }else if(photos.get(i).id.equals(to)){
                toIndex =i;
            }
        }

        photos.add(toIndex, photos.get(fromIndex));
        if(fromIndex < toIndex){
            photos.remove(fromIndex);
        }else{
            photos.remove(fromIndex + 1);
        }
        adapter.update(photos);
    }
}
