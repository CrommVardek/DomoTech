package com.example.My_Home_Controller;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Axel on 07-04-16.
 */
public class Temporary {

    public static void populateSpices (ListView liste, Context context){
        List<String> listeEpices = new ArrayList<String>();

        listeEpices.add("Sel");
        listeEpices.add("Poivre");
        listeEpices.add("Paprika");
        listeEpices.add("Cayenne");
        listeEpices.add("Fines Herbes");
        listeEpices.add("Sel");
        listeEpices.add("Poivre");
        listeEpices.add("Paprika");
        listeEpices.add("Cayenne");
        listeEpices.add("Fines Herbes");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listeEpices);
        liste.setAdapter(adapter);
    }



    public static void populateSpots (ListView liste, Context context){
        List<String> listeEpices = new ArrayList<String>();

        listeEpices.add("Emplacement 1");
        listeEpices.add("Emplacement 2");
        listeEpices.add("Emplacement 3");
        listeEpices.add("Emplacement 4");
        listeEpices.add("Emplacement 5");
        listeEpices.add("Emplacement 6");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listeEpices);
        liste.setAdapter(adapter);
    }
}