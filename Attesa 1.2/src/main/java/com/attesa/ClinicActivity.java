package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClinicActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText cname;
    private ProgressBar progressBar;

    DatabaseReference databaseClinics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);

        cname = (EditText) findViewById(R.id.etCName);

        progressBar = findViewById(R.id.progressbarClinic);
        progressBar.setVisibility(View.GONE);

        // Select our database object to work with.
        databaseClinics = FirebaseDatabase.getInstance().getReference("Clinics");

        findViewById(R.id.btnAddClinic).setOnClickListener(this);

    }

    private void registerClinic() {

        final String name = cname.getText().toString().trim();
        final String ctype = getString(R.string.str_testWalkin);
        final String cphone = getString(R.string.str_testPhone);
        final String clive = "5";
        final int cstatus = 1; // online status

        // Error checking
        if (name.isEmpty()) {
            cname.setError(getString(R.string.input_error_name));
            cname.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(name)){
            String cid = databaseClinics.push().getKey(); // create a unique id in clinics object

            Clinic clinic1 = new Clinic(cid, name, ctype, cphone, clive, cstatus);

            databaseClinics.child(cid).setValue(clinic1);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.str_clinic_added, Toast.LENGTH_LONG).show();
            Intent i = new Intent(ClinicActivity.this, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
        else
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.str_enter_clinic_name, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddClinic:
                registerClinic();
                break;
        }
    }
}
