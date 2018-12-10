package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText loginEmail, loginPass;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        loginEmail = (EditText) findViewById(R.id.edit_text_lemail);
        loginPass = (EditText) findViewById(R.id.edit_text_lpassword);

        progressBar = findViewById(R.id.progressbarLogin);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_login).setOnClickListener(this);

        checkSharedPrefs();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            if(mPreferences.contains("email") && mPreferences.contains("pass")) {

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
        }
    }

    private void loginUser() {

        final String email = loginEmail.getText().toString().trim();
        String password = loginPass.getText().toString().trim();


        // Error checking
        if (email.isEmpty()) {
            loginEmail.setError(getString(R.string.input_error_email));
            loginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError(getString(R.string.input_error_email_invalid));
            loginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPass.setError(getString(R.string.input_error_password));
            loginPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Login(email, password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                loginUser();
                break;
        }
    }

    private void checkSharedPrefs()
    {
        if(mPreferences.contains("email") && mPreferences.contains("pass")) {
            String chkEmail = mPreferences.getString("email", "0");
            String chkPass = mPreferences.getString("pass", "0");
            Login(chkEmail, chkPass);
        }
    }

    private void Login(String email, String pass)
    {
        final String spEmail = email;
        final String spPass = pass;


        // Task to perform login to Firebase DB.
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Successfull login
                    progressBar.setVisibility(View.GONE);

                    mEditor.putString("email", spEmail);
                    mEditor.putString("pass", spPass);
                    mEditor.commit();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {
                    // Failed login
                    Toast.makeText(getApplicationContext(), R.string.str_incorrectCreds, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }



}
