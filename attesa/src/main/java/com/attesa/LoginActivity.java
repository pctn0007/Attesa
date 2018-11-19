package com.attesa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends Activity {

    DatabaseHelper db;

    EditText etEmail;
    EditText etPass;

    Button btnLogin;
    Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etEmail = (EditText)findViewById(R.id.etLEmail);
        etPass = (EditText)findViewById(R.id.etLPass);
        btnLogin = (Button)findViewById(R.id.btnLoginSubmit);
        btnRegister = (Button)findViewById(R.id.btnRegister);


        btnLoginClick();
        btnRegisterClick();

    }

    private void btnRegisterClick()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to Register activity.
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnLoginClick()
    {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                Boolean chkLogin = db.loginSubmit(email, pass);
                if (chkLogin)
                {
                   // Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);


                    String uHC = db.getHCviaEmail(email, pass);
                    intent.putExtra("uHC", uHC);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Incorrect login credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
