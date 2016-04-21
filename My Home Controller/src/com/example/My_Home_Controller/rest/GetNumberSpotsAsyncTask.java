package com.example.My_Home_Controller.rest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Axel on 13-04-16.
 */
public class GetNumberSpotsAsyncTask extends AsyncTask<ListView,Void,Boolean> {

    private final String LOGGER_TAG = "GetNumberSpotsTask";
    private final List<String> spotsList = new ArrayList<String>();
    private final Integer ERROR_VALUE = 0;

    private Integer numberSpots = ERROR_VALUE;
    private ListView spotsListView;

    protected Boolean doInBackground(ListView... views){
        try{
            spotsListView = views[0];
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(Config.getInstance().getSpotsUrl());
            get.setHeader("Content-type","application/json");
            get.setHeader("Accept","application/json");
            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == 200){
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                numberSpots = extractNumberSpotsFromJson(json);
                return true;
            }
            else{
                Log.d(LOGGER_TAG,"Status Code = "+response.getStatusLine().getStatusCode());
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
        for (int i = 1; i<=numberSpots;i++){
            spotsList.add("Emplacement "+i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spotsListView.getContext(), android.R.layout.simple_list_item_1, spotsList);
        spotsListView.setAdapter(adapter);
    }


    private Integer extractNumberSpotsFromJson(String plainText){
        try{
            JSONObject json = new JSONObject(plainText);
            return json.getInt("value");
        } catch (Exception e){
            Log.d(LOGGER_TAG, e.getMessage());
            return ERROR_VALUE;
        }
    }
}