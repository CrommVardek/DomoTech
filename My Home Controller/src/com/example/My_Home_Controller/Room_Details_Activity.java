package com.example.My_Home_Controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.example.My_Home_Controller.rest.*;

/**
 * Handles the control of a room.
 * Shows details about the room and allows the user to change temperature and other data.
 *
 * Created by Axel on 06-03-16.
 */
public class Room_Details_Activity extends Template_Activity implements AdapterView.OnItemClickListener{

    // Views used programmatically
    EditText desiredTemperatureValue;
    TextView desiredLightValue;
    SeekBar desiredLightSeekbar;
    ToggleButton manuel_auto_toggle;
    private ViewAnimator viewAnimator;
    LinearLayout spices_selection;
    LinearLayout spot_selection;
    ListView listeEpices;
    ListView listSpots;
    TextView currentInsideTemperature;
    TextView currentInsideLuminosity;

    // Constants and Variables
    private final String chosen_room_extra_label = "chosenRoom";
    private float lastX;
    String currentRoom;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_details_layout);
        // Set the label of the activity which containts the name of the room.
        String label = getResources().getText(R.string.room_details_label).toString();
        currentRoom = getIntent().getStringExtra(chosen_room_extra_label);
        getActionBar().setTitle(label+currentRoom);

        viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
        manuel_auto_toggle = (ToggleButton) findViewById(R.id.manuel_auto_toggle);
        desiredTemperatureValue = (EditText) findViewById(R.id.desired_temperature_value);
        desiredLightValue = (TextView) findViewById(R.id.desired_light_value);
        desiredLightSeekbar = (SeekBar) findViewById(R.id.desired_light_seekbar);
        spices_selection = (LinearLayout) findViewById(R.id.spices_selection_layout);
        spot_selection = (LinearLayout) findViewById(R.id.spot_selection_layout);
        listeEpices = (ListView) findViewById(R.id.spices_list);
        listSpots = (ListView) findViewById(R.id.spot_list);
        currentInsideTemperature = (TextView) findViewById(R.id.current_temperature_value);
        currentInsideLuminosity = (TextView) findViewById(R.id.current_light_value);

        // OnClickListener for ListViews (Spices and Spots)
        listeEpices.setOnItemClickListener(this);
        listSpots.setOnItemClickListener(this);

        // Seekbar listener to update text value on sliding.
        desiredLightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredLightValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        // Clear content of EditText when focused.
        desiredTemperatureValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    desiredTemperatureValue.setText("");
                } else{
                    desiredTemperatureValue.setText(R.string.default_temperature);
                }
            }});


        // Asynchronous tasks
        new GetNumberSpotsAsyncTask().execute(listSpots);
        new GetInsideLuminosityAsyncTask().execute(currentInsideLuminosity);
        new GetInsideTemperatureAsyncTask().execute(currentInsideTemperature);
        new GetListSpicesAsyncTask().execute(listeEpices);
    }


    /**
     * Handles the switching between the different views (Temperature, Ligth and Spices).
     *
     * @param touchEvent Detected touch movement
     * @return False.
     */
    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:   lastX = touchEvent.getX();
                                            break;
            case MotionEvent.ACTION_UP:
                float currentX = touchEvent.getX();
                // Handling left to right screen swap.
                if (lastX < currentX) {
                    // If there aren't any other children, just break.
                    if (viewAnimator.getDisplayedChild() == 0) break;
                    if (viewAnimator.getDisplayedChild() <= 2) manuel_auto_toggle.setVisibility(View.VISIBLE);

                    viewAnimator.setInAnimation(this, R.anim.slide_in_from_left);
                    viewAnimator.setOutAnimation(this, R.anim.slide_out_to_right);

                    viewAnimator.showPrevious();
                }
                // Handling right to left screen swap.
                if (lastX > currentX) {
                    // If there isn't a child (to the left), just break.
                    if (!currentRoom.equals("Cuisine") && viewAnimator.getDisplayedChild() == 1) break;
                    if (viewAnimator.getDisplayedChild() == 2) break;
                    // If we switch to the Spices selection, hide the auto/manual toggle
                    if(viewAnimator.getDisplayedChild() == 1) manuel_auto_toggle.setVisibility(View.GONE);

                    viewAnimator.setInAnimation(this, R.anim.slide_in_from_right);
                    viewAnimator.setOutAnimation(this, R.anim.slide_out_to_left);

                    viewAnimator.showNext();
                }
                break;
        }
        return false;
        }


    /**
     * Increases the value of the desired temperature. (Max value is 25)
     *
     * @param view Button pressed (must only be the "up" button).
     */
    public void temperatureIncrease(View view){
        double oldValue = Double.parseDouble(desiredTemperatureValue.getText().toString());
        double newValue = oldValue + 0.5;

        if(newValue > 25){
            Toast.makeText(Room_Details_Activity.this, "Valeur maximale atteinte...", Toast.LENGTH_SHORT).show();
        } else{
            desiredTemperatureValue.setText(String.valueOf(newValue));
        }
    }

    /**
     * Decreases the value of the desired temperature. (Min value is 15)
     *
     * @param view Button pressed (must only be the "down" button)
     */
    public void temperatureDecrease(View view){
        double oldValue = Double.parseDouble(desiredTemperatureValue.getText().toString());
        double newValue = oldValue - 0.5;

        if(newValue < 15){
            Toast.makeText(Room_Details_Activity.this, "Valeur minimale atteinte.", Toast.LENGTH_SHORT).show();
        } else {
            desiredTemperatureValue.setText(String.valueOf(newValue));
        }
    }

    /**
     * Handles the toggle button of room_details_layout.
     * When in Automatic mode, only current interior and exterior temperature must be visible.
     * When in Manual mode, both current temperature and desired temperature must be visible.
     *
     * @param view Button pressed (must only be toggle button).
     */
    public void handleToggle(View view){
        ToggleButton toggle = (ToggleButton) view;
        LinearLayout desiredTemperature = (LinearLayout) findViewById(R.id.temperature_Manual_layout);
        LinearLayout desiredLight = (LinearLayout) findViewById(R.id.light_Manual_layout);
        // Display desired temperature
        if (toggle.isChecked()){
            desiredTemperature.setVisibility(View.VISIBLE);
            desiredLight.setVisibility(View.VISIBLE);
        } else{
            desiredTemperature.setVisibility(View.INVISIBLE);
            desiredLight.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Handles the selection of a radio button.
     * If the spices selection radio button is selected then:   - displays the list of spices available
     *                                                          - asks the user to select one
     * If the spot selection radio button is selected then:     - displays the range of possibilites
     *                                                          - asks the user to select one
     *
     * @param view RadioButton clicked.
     */
    public void onRadioButtonSelected(View view){
        RadioButton button = (RadioButton) view;
        if (button.getText().toString().equals("Épices")){
            spices_selection.setVisibility(View.VISIBLE);
            spot_selection.setVisibility(View.GONE);
        }
        if (button.getText().toString().equals("Emplacement")){
            spices_selection.setVisibility(View.GONE);
            spot_selection.setVisibility(View.VISIBLE);
        }
    }


    public void onItemClick(AdapterView<?> l, View view, int position, long id){
        String item = (String) l.getAdapter().getItem(position);
        if (findViewById(((ListView) l).getId()).equals(listeEpices)){
            PostDesiredSpiceAsyncTask task = new PostDesiredSpiceAsyncTask(Room_Details_Activity.this);
            task.execute(item);
        } else if (findViewById(((ListView) l).getId()).equals(listSpots))
        {
            Integer value = Integer.valueOf(item.split(" ")[1]);
            new PostDesiredSpotAsyncTask().execute(Integer.valueOf(value));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.synchronise:  new GetNumberSpotsAsyncTask().execute(listSpots);
                                    new GetInsideLuminosityAsyncTask().execute(currentInsideLuminosity);
                                    new GetInsideTemperatureAsyncTask().execute(currentInsideTemperature);
                                    new GetListSpicesAsyncTask().execute(listeEpices);
                                    break;
            default:break;
        }
        return true;
    }


    public void submitValue(View view){
        if (view.getId() == R.id.submitLuminosity){
            if (desiredLightValue != null && desiredLightValue.getText() != null && desiredLightValue.getText().toString() != "" ){
                new PostDesiredLuminosityAsyncTask().execute(Integer.valueOf(desiredLightValue.getText().toString()));
            } else{
                Toast.makeText(Room_Details_Activity.this, "Veuillez entrer une valeur de luminosité.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId() == R.id.submitTemperature){
            if (desiredTemperatureValue != null && desiredTemperatureValue.getText() != null && desiredTemperatureValue.getText().toString() != "" ){
                new PostDesiredTemperatureAsyncTask().execute(Double.valueOf(desiredTemperatureValue.getText().toString()));
            } else{
                Toast.makeText(Room_Details_Activity.this, "Veuillez entrer une valeur de température.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            // Error
            Toast.makeText(Room_Details_Activity.this, "An error occured in submit value....", Toast.LENGTH_SHORT).show();
        }
    }
}

