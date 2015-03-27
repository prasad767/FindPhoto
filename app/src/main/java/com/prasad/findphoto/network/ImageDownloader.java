

package com.prasad.findphoto.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.prasad.findphoto.callback.ImageRefresh;
import com.prasad.findphoto.dao.Photo;
import com.prasad.findphoto.utils.ImageStore;


/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class ImageDownloader extends AsyncTask<ImageView, Void, Bitmap> {

    ImageView imgView;
    ImageRefresh refresh;
    public ImageDownloader(){

    }
    public ImageDownloader(ImageRefresh refresh){
      this.refresh = refresh;
    }

    protected Bitmap doInBackground(ImageView... imgView) {
        Bitmap bitmap = null;
        try {
         this.imgView = imgView[0];
         bitmap = BitmapFactory.decodeStream((InputStream) new URL(this.imgView.getTag().toString())
                    .getContent());
        }
        catch(IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        } finally {

        }
        return bitmap;
    }

    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Bitmap bitmap) {

        if(bitmap != null ){
            imgView.setImageBitmap(bitmap);
            ImageStore.getInstance().addBitmapToCache(imgView.getTag().toString(), bitmap);
        }
    }
}
