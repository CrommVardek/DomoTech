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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Axel on 13-04-16.
 */
public class GetListSpicesAsyncTask extends AsyncTask<ListView,Void,Boolean> {

    private final String LOGGER_TAG = "GetListSpicesTask";
    private final List<String> ERROR_VALUE = Arrays.asList("Error...");

    // TODO: Change with Spice object
    private List<String> spicesList = new ArrayList<String>();
    private ListView spicesListView;




    protected Boolean doInBackground(ListView... views){
        try {
            spicesListView = views[0];
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(Config.getInstance().getSpicesUrl());
            get.setHeader("Content-type","application/json");
            get.setHeader("Accept","application/json");
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200){
                String json = EntityUtils.toString(response.getEntity(),"UTF-8");
                spicesList = extractSpicesFromJson(json);
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


    protected void onProgressUpdate(Void... progress){}

    protected void onPostExecute(Boolean result){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spicesListView.getContext(), android.R.layout.simple_list_item_1, spicesList);
        spicesListView.setAdapter(adapter);
    }


    private List<String> extractSpicesFromJson(String plainText){
        try{
            List<String> res = new ArrayList<String>();
            JSONArray array = new JSONArray(plainText);
            for (int i=0; i<array.length(); i++){
                JSONObject jsonObject = array.getJSONObject(i);
                res.add(jsonObject.getString("name"));
            }
            return res;
        } catch (JSONException e){
            Log.d(LOGGER_TAG, e.getMessage());
            return ERROR_VALUE;
        }
    }
}
