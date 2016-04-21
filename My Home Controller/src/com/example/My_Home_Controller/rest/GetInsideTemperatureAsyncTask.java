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
 * Asynchronous task to get the inside temperature of the house (of a specific room).
 * Contacts the server to get the temperature value; if an error occurs, the default value is ERROR_VALUE.
 *
 * Created by Axel on 12-04-16.
 */
public class GetInsideTemperatureAsyncTask extends AsyncTask<TextView, Void, Boolean> {

    private final String LOGGER_TAG = "GetInsideTempTask";
    private final Double ERROR_VALUE = 999.99;

    private Double currentInsideTemp=ERROR_VALUE;
    private TextView insideTemperatureTextView;

    protected Boolean doInBackground(TextView... args) {
        try{
            insideTemperatureTextView = args[0];
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(Config.getInstance().getInsideTemperatureUrl());
            get.setHeader("Content-type", "application/json");
            get.setHeader("Accept", "application/json");
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                Double insideTemperature = extractTemperatureFromJson(json);
                currentInsideTemp = insideTemperature;
                return true;
            }else{
                Log.d(LOGGER_TAG, "Status Code = "+response.getStatusLine().getStatusCode());
                return false;
            }
        } catch (Exception e){
            Log.d(LOGGER_TAG,e.getMessage());
            return false;
        }
    }

    protected void onProgressUpdate(Void... progress) {

    }

    protected void onPostExecute(Boolean result) {
        insideTemperatureTextView.setText(String.valueOf(currentInsideTemp));
    }

    /**
     * Extracts the temperature value of the JSON given by the server.
     *
     * @param plainText String value of the JSON.
     * @return The temperature value of the JSON in a Double Object.
     */
    private Double extractTemperatureFromJson(String plainText){
        try {
            JSONObject json = new JSONObject(plainText);
            return json.getDouble("value");
        } catch (JSONException e){
            Log.d(LOGGER_TAG,e.getMessage());
            return ERROR_VALUE;
        }
    }
}