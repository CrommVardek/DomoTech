package com.example.My_Home_Controller;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Handles functionalities common to all activities.
 * For now, it handles the menus.
 *
 * Created by Axel on 05-03-16.
 */
public class Template_Activity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:   handleLogout();
                                break;
            default:break;
        }
        return true;
    }


    /**
     * Handles the login out of the application.
     * Terminates the current session to prevent unauthorised usage.
     */
    private void handleLogout(){
        Intent nextIntent = new Intent(Template_Activity.this, Login_Activity.class);
        startActivity(nextIntent);
    }
}
