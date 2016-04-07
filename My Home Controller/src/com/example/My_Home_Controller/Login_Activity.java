package com.example.My_Home_Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Handles the login activity of the application.
 * The user needs to authenticate to use the app.
 * A successful authentication leads to Home_Activity.
 *
 * Created by Axel on 05-03-16.
 */
public class Login_Activity extends Activity {

    private EditText login_username;
    private EditText login_password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }


    /**
     * Handles the validation of the login form.
     * If the form is filled and the data are correct: redirect to home page.
     * If the form is blank or the data are incorrect: stays on this activity and display an error message.
     *
     * @param view Button pressed (in this case, should be the confirm button of the login activity).
     */
    public void handleLogin(View view){
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);

        final String login = login_username.getText().toString();
        final String password = login_password.getText().toString();
        
        if(login == null || login.equals("")){
            Toast.makeText(Login_Activity.this, "Veuillez entrer un nom d'utilisateur, s'il vous plaît.", Toast.LENGTH_SHORT).show();
        }

        else if(password == null || password.equals("")){
            Toast.makeText(Login_Activity.this, "Veuillez entrer un mot de passe, s'il vous plaît.", Toast.LENGTH_SHORT).show();
        }

        else if (!( (login.equals("user")) && (password.equals("user")) )){
            Toast.makeText(Login_Activity.this, "Le nom d'utilisateur et le mot de passe ne correspondent pas. \nMerci de réessayer.", Toast.LENGTH_SHORT).show();
        }

        else {
            Intent nextScreen = new Intent(Login_Activity.this, Home_Activity.class);
            startActivity(nextScreen);
        }
    }
}
