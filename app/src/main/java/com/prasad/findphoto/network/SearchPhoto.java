package com.prasad.findphoto.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.prasad.findphoto.callback.ResponseCallback;
import com.prasad.findphoto.dao.Photos;
import com.prasad.findphoto.dao.PublicPhoto;
import com.prasad.findphoto.utils.Constans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prasad Rathinasabapathi on 26-Mar-15.
 */
public class SearchPhoto extends AsyncTask<String, Integer, String> {

    private ResponseCallback callback;
    public SearchPhoto(ResponseCallback callback){
        this.callback = callback;
    }

    protected String doInBackground(String... search) {
        HttpURLConnection urlConnection = null;
        String response = null;
        try {
            URL url = new URL(String.format("https://api.flickr.com/services/rest/?&method=flickr.photos.search&format=json&api_key=74133bbd7fbc945b9cf5da5ad64e719e&tags=%s&per_page=10",search[0]));

            Log.v("URL>>",url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            int status = urlConnection.getResponseCode();
            Log.v("status code>>",status+"");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response  = readStream(in);
            Log.v("Response>>",response);

        }
        catch(IOException e){
            e.printStackTrace();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            Log.v("Network","finally");
        }
        return response;
    }

    protected void onProgressUpdate(Integer... progress) {
       // setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String result) {

        if(callback != null && result != null){
            callback.onSuccess(result);
        }
    }


    private String readStream(InputStream in) throws IOException{
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }
}
