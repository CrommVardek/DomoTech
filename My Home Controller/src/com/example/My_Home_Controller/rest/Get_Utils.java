package com.example.My_Home_Controller.rest;

/**
 * Created by Axel on 12-04-16.
 */
public class Get_Utils {

    public static boolean getInsideTemperature(){


        return true;
    }


}



/*
    public static boolean syncFeuilleRoute() {
        try {
            return new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... args) {
                    AuthInfo instance = AuthInfo.getInstance();
                    try {
                        if (instance != null) {
                            HttpClient client = new DefaultHttpClient();
                            HttpGet get = new HttpGet(AuthInfo.getInstance().GET_ROADMAP_URL);
                            get.addHeader("service_key", AuthInfo.getInstance().getServiceKey());
                            get.addHeader("auth_token", AuthInfo.getInstance().getAuthToken());
                            get.setHeader("Content-type", "application/json");
                            get.setHeader("Accept", "application/json");
                            HttpResponse response = client.execute(get);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                String json_Response = EntityUtils.toString(response.getEntity(), "UTF-8");
                                JSONObject json = new JSONObject(json_Response);
                                new TraitementFeuileRoute().SaveFeuille(json_Response);
                                if(json.getBoolean("intra")){
                                    return true;
                                }
                            }
                            return false;
                        }
                    }catch(Exception e){

                        Log.d("SyncUtil.states", e.getMessage());
                    }
                    return false;
                }
            }.execute().get();
        }catch(Exception e){
            Log.d("SyncUtil.states", e.getMessage());
        }
        return false;
    }
 */



// POST
/*
    public static void syncStates() {
        Iterator<Etat> it = SugarRecord.findAsIterator(Etat.class, "is_colis=? and centre_etat = 0", "1");
        List<Etat> etats = new ArrayList<>();
        while (it.hasNext()) {
            etats.add(it.next());
        }
        List<EtatRepresentation> representations = new ArrayList<>();
        for (Etat e: etats) {
            representations.add(new EtatRepresentation(e));
        }
        Gson gson = new Gson();
        final String json = gson.toJson(representations);

        try {
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... args) {
                    AuthInfo instance = AuthInfo.getInstance();
                    try {
                        if (instance != null) {
                            HttpClient client = new DefaultHttpClient();
                            HttpPost post = new HttpPost(AuthInfo.getInstance().STATE_SYNC_URL);
                            post.addHeader("service_key", AuthInfo.getInstance().getServiceKey());
                            post.addHeader("auth_token", AuthInfo.getInstance().getAuthToken());
                            post.addHeader("Content-type", "application/json");
                            HttpEntity entity = new StringEntity(json);
                            post.setEntity(entity);
                            HttpResponse response = client.execute(post);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                SugarRecord.deleteAll(Etat.class);
                                SugarRecord.deleteAll(Adresse.class);
                                SugarRecord.deleteAll(Client.class);
                                SugarRecord.deleteAll(Chariot.class);
                                SugarRecord.deleteAll(Mensurations.class);
                                SugarRecord.deleteAll(Colis.class);
                                SugarRecord.deleteAll(Centre.class);
                            }
                        }
                    }catch(Exception e){
                        Log.d("SyncUtil.states", e.getMessage());
                    }
                    return null;
                }
            }.execute().get();
        }catch(Exception e){
            Log.d("SyncUtil.states", e.getMessage());
        }

    }
 */