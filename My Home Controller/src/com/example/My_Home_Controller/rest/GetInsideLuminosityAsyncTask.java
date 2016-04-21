package com.example.My_Home_Controller.rest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Axel on 13-04-16.
 */
public class GetInsideLuminosityAsyncTask extends AsyncTask<TextView, Void, Boolean> {

    private final String LOGGER_TAG = "GetInsideLightTask";
    private final Integer ERROR_VALUE = -1;

    private Integer currentInsideLuminosity = ERROR_VALUE;
    private TextView insideLuminosityTextView;

    protected Boolean doInBackground(TextView... views){
        try{
            insideLuminosityTextView = views[0];
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(Config.getInstance().getInsideLuminosityUrl());
            get.setHeader("Content-type", "application/json");
            get.setHeader("Accept","application/json");
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200){
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                currentInsideLuminosity = extractLuminosityFromJson(json);
                return true;
            } else{
                Log.d(LOGGER_TAG, "Status Code = "+response.getStatusLine().getStatusCode());
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
        insideLuminosityTextView.setText(String.valueOf(currentInsideLuminosity));
    }


    private Integer extractLuminosityFromJson(String plainText){
        try {
            JSONObject json = new JSONObject(plainText);
            return json.getInt("value");
        } catch (JSONException e){
            Log.d(LOGGER_TAG,e.getMessage());
            return ERROR_VALUE;
        }
    }
}
