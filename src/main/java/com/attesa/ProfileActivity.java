package com.attesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private String curUID, phone;
    private TextView tvname, tvbio, tvemail;
    private ImageButton btnEditProfile;

    private DatabaseReference databaseUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnEditProfile = (ImageButton) findViewById(R.id.btnEditProfile);

        tvname = (TextView) findViewById(R.id.tvName);
        tvbio = (TextView) findViewById(R.id.tvBio);
        tvemail = (TextView) findViewById(R.id.tvEmail);

        progressBar = findViewById(R.id.progressbarProfile);
        progressBar.setVisibility(View.GONE);

        // Select our database object to work with.
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });


        getProfile();
    }

    private void getProfile() {

        final String strName = tvname.getText().toString().trim();
        final String strEmail = tvemail.getText().toString().trim();

        // Error checking
        if (strName.isEmpty()) {
            tvname.setText(R.string.str_unknown);
            return;
        }
        // Error checking
        if (strEmail.isEmpty()) {
            tvemail.setText(R.string.str_unknown);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        // Get user account info.
        mAuth = FirebaseAuth.getInstance();
        curUID = mAuth.getCurrentUser().getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(curUID);

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String uemail = dataSnapshot.child("email").getValue().toString();
                    String uname = dataSnapshot.child("name").getValue().toString();
                    String ubio = dataSnapshot.child("bio").getValue().toString();
                    String uphone = dataSnapshot.child("phone").getValue().toString();


                    phone = uphone;
                    tvname.setText(uname);
                    tvemail.setText(uemail);
                    tvbio.setText(ubio);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        progressBar.setVisibility(View.GONE);

    }

    private void editProfile()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.editprofile_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        final Button btnSaveProfile= (Button) dialogView.findViewById(R.id.btnSaveProfile);

        final EditText etEditName = (EditText) dialogView.findViewById(R.id.etEditName);
        final EditText etEditEmail= (EditText) dialogView.findViewById(R.id.etEditEmail);
        final EditText etEditBio = (EditText) dialogView.findViewById(R.id.etEditBio);
        final EditText etEditPhone = (EditText) dialogView.findViewById(R.id.etEditPhone);

        etEditName.setText(tvname.getText().toString());
        etEditEmail.setText(tvemail.getText().toString());
        etEditBio.setText(tvbio.getText().toString());
        etEditPhone.setText(phone);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mAuth = FirebaseAuth.getInstance();
                curUID = mAuth.getCurrentUser().getUid();
                databaseUser = FirebaseDatabase.getInstance().getReference("Users");

                // Update user account status.
                String cid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                String dirName = databaseUser.child(cid).child("name").getKey();
                databaseUser.child(cid).child(dirName).setValue(etEditName.getText().toString());

                String dirEmail = databaseUser.child(cid).child("email").getKey();
                databaseUser.child(cid).child(dirEmail).setValue(etEditEmail.getText().toString());

                String dirBio = databaseUser.child(cid).child("bio").getKey();
                databaseUser.child(cid).child(dirBio).setValue(etEditBio.getText().toString());

                String dirPhone = databaseUser.child(cid).child("phone").getKey();
                databaseUser.child(cid).child(dirPhone).setValue(etEditPhone.getText().toString());

                Toast.makeText(getApplicationContext(), R.string.str_editprosuccess, Toast.LENGTH_LONG).show();
                Intent i = new Intent(ProfileActivity.this, ProfileActivity.class); // reload page to reassure new info is displayed
                startActivity(i);
                alertDialog.cancel();
                finish();

            }
        });


        alertDialog.show();
    }


}
