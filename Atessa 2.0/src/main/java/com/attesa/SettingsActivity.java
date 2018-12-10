package com.attesa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText etHC1;
    private EditText etHC2;

    private EditText etPass1;
    private EditText etPass2;
    private EditText etPass3;

    private Button btnSaveHC;
    private Button btnSavePass;

    private DatabaseReference databaseSettings;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        mAuth = FirebaseAuth.getInstance();

        etHC1 = (EditText) findViewById(R.id.etSetHC);
        etHC2 = (EditText) findViewById(R.id.etSetHC2);

        etPass1 = (EditText) findViewById(R.id.etSetPass1);
        etPass2 = (EditText) findViewById(R.id.etSetPass2);
        etPass3 = (EditText) findViewById(R.id.etSetPass3);

        btnSaveHC = (Button) findViewById(R.id.btnSaveHC);
        btnSavePass = (Button) findViewById(R.id.btnSavePass);

        btnSaveHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHC();
            }
        });

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePass();
            }
        });

    }

    // Update out new hc
    private void updateHC()
    {

        String chkPass = mPreferences.getString("pass", "0");

        String strHC1 = etHC1.getText().toString();
        String strHC2 = etHC2.getText().toString();

        // error handle
        if (strHC1.equals("") || strHC2.equals("") || strHC1.length() < 10)
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid health card number and confirm your passwod!", Toast.LENGTH_LONG).show();
        }
        else if (!strHC2.equals(chkPass))
        {
            Toast.makeText(getApplicationContext(), "The password you have entered is incorrect!", Toast.LENGTH_LONG).show();
        }
        else if (strHC1.length() == 10 && strHC2.equals(chkPass))
        {
            // Update user account status.
            databaseSettings = FirebaseDatabase.getInstance().getReference("Users");
            String cid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String dir = databaseSettings.child(cid).child("HC").getKey();
            databaseSettings.child(cid).child(dir).setValue(strHC1);
            Toast.makeText(getApplicationContext(), "Health card updated successfully!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Oops! Something went wrong! We are working hard to fix this issue.", Toast.LENGTH_LONG).show();
        }

    }

    // Update our new password.
    private void updatePass()
    {
        String chkPass = mPreferences.getString("pass", "0");
        String strPass1 = etPass1.getText().toString();
        String strPass2 = etPass2.getText().toString();
        String strPass3 = etPass3.getText().toString();

        // error handle
        if (strPass1.length() < 1 || strPass2.length() < 1 || strPass3.length() < 1)
        {
            Toast.makeText(getApplicationContext(), "All password fields must be filled out!", Toast.LENGTH_LONG).show();
        }
        else if (!strPass2.equals(strPass3))
        {
            Toast.makeText(getApplicationContext(), "Your new password does not match the confirmation field!", Toast.LENGTH_LONG).show();
        }
        else if (strPass1.equals(chkPass) && strPass2.equals(strPass3))
        {
            mEditor.putString("pass", strPass2);
            mEditor.commit();
            Toast.makeText(getApplicationContext(), "Password updated successfully!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Oops! Something went wrong! We are working hard to fix this issue.", Toast.LENGTH_LONG).show();
        }

    }

}
