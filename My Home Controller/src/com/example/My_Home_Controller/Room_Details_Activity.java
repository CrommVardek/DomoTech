package com.example.My_Home_Controller;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

/**
 * Handles the control of a room.
 * Shows details about the room and allows the user to change temperature and other data.
 *
 * Created by Axel on 06-03-16.
 */
public class Room_Details_Activity extends Template_Activity {

    TextView ROOM_DETAILS_LABEL;
    EditText desiredTemperatureValue;
    TextView desiredLightValue;
    SeekBar desiredLightSeekbar;
    private final String chosen_room_extra_label = "chosenRoom";


    private ViewAnimator viewAnimator;
    private float lastX;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_details_layout);

        String label = getResources().getText(R.string.room_details_label).toString();
        String room = getIntent().getStringExtra(chosen_room_extra_label);
        getActionBar().setTitle(label+room);

        viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);

        ROOM_DETAILS_LABEL = (TextView) findViewById(R.id.room_details_label);
        ROOM_DETAILS_LABEL.setText(getIntent().getStringExtra(chosen_room_extra_label));

        desiredTemperatureValue = (EditText) findViewById(R.id.desired_temperature_value);
        desiredLightValue = (TextView) findViewById(R.id.desired_light_value);
        desiredLightSeekbar = (SeekBar) findViewById(R.id.desired_light_seekbar);

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

                    viewAnimator.setInAnimation(this, R.anim.slide_in_from_left);
                    viewAnimator.setOutAnimation(this, R.anim.slide_out_to_right);

                    viewAnimator.showPrevious();
                }
                // Handling right to left screen swap.
                if (lastX > currentX) {
                    // If there isn't a child (to the left), just break.
                    if (viewAnimator.getDisplayedChild() == 2) break;

                    viewAnimator.setInAnimation(this, R.anim.slide_in_from_right);
                    viewAnimator.setOutAnimation(this, R.anim.slide_out_to_left);

                    viewAnimator.showNext();
                }
                break;
        }
        return false;
        }


    /**
     * Increases the value of the desired temperature.
     *
     * @param view Button pressed (must only be the "up" button).
     */
    public void temperatureIncrease(View view){
        // TODO:    Send request to server
        //          Add checks on the value
        //          Decide max and min value
        double oldValue = Double.parseDouble(desiredTemperatureValue.getText().toString());
        double newValue = oldValue + 0.5;
        desiredTemperatureValue.setText(String.valueOf(newValue));
    }

    /**
     * Decreases the value of the desired temperature.
     *
     * @param view Button pressed (must only be the "down" button)
     */
    public void temperatureDecrease(View view){
        // TODO: Send request to server
        double oldValue = Double.parseDouble(desiredTemperatureValue.getText().toString());
        double newValue = oldValue - 0.5;
        desiredTemperatureValue.setText(String.valueOf(newValue));
    }

    /**
     * Handles the toggle button of room_details_layout.
     * When in Automatic mode, only current interior and exterior temperature must be visible.
     * When in Manual mode, both current temperature and desired temperature must be visible.
     *
     * @param view Button pressed (must only be toggle button).
     */
    public void handleToggle(View view){
        // TODO: Request new value when displaying the linear layout
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
}

