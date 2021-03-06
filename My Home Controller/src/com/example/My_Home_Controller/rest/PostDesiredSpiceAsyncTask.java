package com.example.My_Home_Controller.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Axel on 13-04-16.
 */
public class PostDesiredSpiceAsyncTask extends AsyncTask<String, Void, Boolean>{

    private String LOGGER_TAG = "PostSpiceAsyncTask";
    private boolean success = false;
    private Context context;

    public PostDesiredSpiceAsyncTask(Context context){
        this.context = context;
    }


    protected Boolean doInBackground(String... args){
        try{
            String json = "{\"name\" : \"" + args[0] + "\"}";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Config.getInstance().getSpicesUrl());
            post.addHeader("Content-type","application/json");
            post.addHeader("Application","application/json");
            HttpEntity entity = new StringEntity(json);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200){
                Log.d(LOGGER_TAG, "JSON sending successful.");
                success = true;
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
        if (success) Toast.makeText(context, "L'épice vous est présentée", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "L'épice n'est malheureusement pas sur la roue.", Toast.LENGTH_SHORT).show();
        Log.d(LOGGER_TAG, "Spice post async task done.");
    }
}