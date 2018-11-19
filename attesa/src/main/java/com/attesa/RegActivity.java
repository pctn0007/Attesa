package com.attesa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegActivity extends AppCompatActivity {

    DatabaseHelper db;

    EditText etEmail;
    EditText etPass;
    EditText etCPass;
    EditText etHC;

    Button btnRegister;
    Button btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        etCPass = (EditText)findViewById(R.id.etPass2);
        etHC = (EditText)findViewById(R.id.etHC);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnRegisterClick();
        btnLoginClick();

    }


    private void btnLoginClick()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnRegisterClick()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strEmail = etEmail.getText().toString();
                String strPass = etPass.getText().toString();
                String strCPass = etCPass.getText().toString();
                String strHC = etHC.getText().toString();

                if (strEmail.equals("") || strPass.equals("") || strCPass.equals("") || strHC.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "All fields must contain data.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (strPass.equals(strCPass))
                    {
                        // account checking
                        Boolean chkHC = db.chkHC(strHC);
                        Boolean chkemail = db.chkemail(strEmail);

                        if (chkemail) // it's true, we dont exist
                        {
                            if (chkHC)
                            {
                                // Add new user record to Database.
                                Boolean insert = db.insert(strEmail, strCPass, strHC);

                                if (insert) // Were we successful?
                                {
                                    // goto login activity
                                   Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                                   startActivity(intent);
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Health Card Number already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
