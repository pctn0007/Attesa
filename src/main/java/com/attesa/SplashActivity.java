package com.attesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();


        // We check if we have a user already logged. If so, skip this activity.
        if(mPreferences.contains("email") && mPreferences.contains("pass")) {

            progressBar = findViewById(R.id.progressbarSplash);
            progressBar.setVisibility(View.VISIBLE);
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            progressBar.setVisibility(View.GONE);
        }

        Button btnSLogin = (Button) findViewById(R.id.btnSLogin);
        Button btnSRegister = (Button) findViewById(R.id.btnSRegister);
        Button btnNewClinic = (Button) findViewById(R.id.btnNewClinic);


        btnSLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, RegActivity.class);
                startActivity(i);
            }
        });

        btnNewClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, ClinicActivity.class);
                startActivity(i);
            }
        });
    }


}
