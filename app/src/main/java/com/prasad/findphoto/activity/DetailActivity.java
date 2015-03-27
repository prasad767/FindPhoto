package com.prasad.findphoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prasad.findphoto.R;
import com.prasad.findphoto.callback.ImageRefresh;
import com.prasad.findphoto.utils.ImageStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class DetailActivity  extends BaseActivity{

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String url = getIntent().getStringExtra("URL");
        String title = getIntent().getStringExtra("TITLE");
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
                ((TextView) findViewById(R.id.text_detail)).setText(title);
        new ImageDownloader().execute(url);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(progress != null)
        progress.dismiss();
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        ImageView imgView;
        ImageRefresh refresh;

        public ImageDownloader() {

        }

        public ImageDownloader(ImageRefresh refresh) {
            this.refresh = refresh;
        }

        protected Bitmap doInBackground(String... url) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(url[0])
                        .getContent());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return bitmap;
        }

        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ((ImageView) findViewById(R.id.image_detail)).setImageBitmap(bitmap);
            }
            progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
