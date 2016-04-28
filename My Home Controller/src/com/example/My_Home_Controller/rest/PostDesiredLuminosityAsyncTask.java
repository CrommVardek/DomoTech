package com.example.My_Home_Controller.rest;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Axel on 13-04-16.
 */
public class PostDesiredLuminosityAsyncTask extends AsyncTask<Integer,Void,Boolean> {

    private final String LOGGER_TAG = "PostLuminosityAsyncTask";

    protected Boolean doInBackground(Integer... args){
        try{
            String json = "{\"value\" : \"" + args[0] + "\"}";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Config.getInstance().getInsideLuminosityUrl());
            post.addHeader("Content-type", "application/json");
            post.addHeader("Accept", "application/json");
            HttpEntity entity = new StringEntity(json);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200){
                Log.d(LOGGER_TAG,"JSON sending successful.");
                return true;
            } else{
                Log.d(LOGGER_TAG,"Status Code: "+response.getStatusLine().getStatusCode());
                return false;
            }
        } catch (Exception e){
            Log.d(LOGGER_TAG, e.getMessage());
            return false;
        }

    }

    protected void onProgressUpdate(Void... progress){

    }

    protected void onPostExecute(Boolean result){
        Log.d(LOGGER_TAG, "Luminosity post async task done.");
    }
}