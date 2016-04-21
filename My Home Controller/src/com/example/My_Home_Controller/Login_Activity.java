package com.example.My_Home_Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
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

        if (!isInternetAvailable()){
            displayDialog();
        }
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


    private boolean isInternetAvailable(){
        try{
/*            InetAddress ipaddr = InetAddress.getByName(Config.getInstance().getBaseUrl());
            if (ipaddr.equals("")){
                return false;
            } else {
                return true;
            }*/

            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && (networkInfo.isConnected()))
            {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }

    private void displayDialog(){
        new AlertDialog.Builder(Login_Activity.this)
                .setTitle("Problème de connectivité")
                .setMessage("Vous devez être connecté à internet pour accéder à cette application;")
                .setPositiveButton(R.string.tryAgain, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isInternetAvailable()){
                            dialog.cancel();
                        } else{
                            displayDialog();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
