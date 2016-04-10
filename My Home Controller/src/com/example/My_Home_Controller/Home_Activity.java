package com.example.My_Home_Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Handles the main activity of the application.
 * This activity displays a plan of the house with the different rooms clickable.
 *
 * Created by Axel on 05-03-16.
 */
public class Home_Activity extends Template_Activity {

    private final String chosen_room_extra_label = "chosenRoom";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        getActionBar().setTitle(getResources().getString(R.string.home_activity_title));
    }


    /**
     * Redirects to the proper activity depending on the button (image) pressed.
     * The different images possible are: kitchen, living-room, bedroom, office.
     *
     * @param view Button (image) pressed.
     */
    public void handleClick(View view){
        Intent intent = new Intent(Home_Activity.this, Room_Details_Activity.class);

        switch (view.getId()){
            case R.id.kitchen_button:   intent.putExtra(chosen_room_extra_label, "Cuisine");
                                        break;
            case R.id.living_room_button:   intent.putExtra(chosen_room_extra_label, "Salon");
                                            break;
            case R.id.bedroom_button:   intent.putExtra(chosen_room_extra_label, "Chambre à coucher");
                                        break;
            case R.id.bathroom_button:    intent.putExtra(chosen_room_extra_label, "Salle de bains");
                                        break;
            default:    break;
        }

        startActivity(intent);
    }
}
