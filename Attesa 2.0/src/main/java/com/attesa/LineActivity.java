package com.attesa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LineActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 30000; // 10 minutes

    private Button btnCancel;
    private TextView tvTimer;
    private CountDownTimer mCounterdown;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReviews;
    private EditText etComment;
    private ImageButton imgTUP;
    private ImageButton imgTDOWN;
    private String strVote;
    private TextView tvLineCName;
    private TextView tvLineCAddr;
    private TextView tvLineCLive;
    private int intCLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews");

        tvLineCName = (TextView) findViewById(R.id.tvLineCName);
        tvLineCAddr = (TextView) findViewById(R.id.tvLineCAddr);
        tvLineCLive = (TextView) findViewById(R.id.tvLineCLive);

        tvTimer = (TextView) findViewById(R.id.tvTimer);

        String strCName = getIntent().getExtras().getString("cname");
        String strCAddr = getIntent().getExtras().getString("caddr");
        String strCPone = getIntent().getExtras().getString("cphone");
        String strCLive = getIntent().getExtras().getString("clive");

        // Convert string to int so we can do math.
        intCLive = Integer.parseInt(strCLive);

        tvLineCName.setText(strCName);
        tvLineCAddr.setText(strCAddr);
        tvLineCLive.setText(strCLive);

        // Start visit countdown time.
        startTime();

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmCancel();

            }
        });

    }

    private void startTime()
    {
        mCounterdown = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished * intCLive;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

                // do something when time reaches zero!
                ReviewDialog();

            }

        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText()
    {
        int minutes = (int)( mTimeLeftInMillis / 1000) / 60;
        int seconds = (int)( mTimeLeftInMillis / 1000) % 60;

        if (minutes <= 5 && seconds == 0)
        {
            tvTimer.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else
        {
            tvTimer.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        @SuppressLint("DefaultLocale")
        String timeLeftFormat = String.format("%02d:%02d", minutes, seconds);

        tvTimer.setText(timeLeftFormat);
    }

    private void confirmCancel()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.cancel_confirm, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText etConfirmCancel = (EditText) dialogView.findViewById(R.id.etCancelPass);
        final Button btnGoBack = (Button) dialogView.findViewById(R.id.btnGoBack);
        final Button btnConfirmCancel = (Button) dialogView.findViewById(R.id.btnConfirmCancel);

        final SharedPreferences mPreferences;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        // Confirm cancel
        btnConfirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPreferences.contains("email") && mPreferences.contains("pass")) {

                    String chkPass = mPreferences.getString("pass", "0");

                    if (etConfirmCancel.getText().toString().equals(chkPass))
                    {
                        Intent i = new Intent(LineActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        alertDialog.cancel();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), R.string.str_oops, Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        // Cancel (go back)
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();

    }




    private void ReviewDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.review_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        final Button btnSendReview = (Button) dialogView.findViewById(R.id.btnSendReview);
        final Button btnSkip = (Button) dialogView.findViewById(R.id.btnSkipReview);

        etComment = (EditText) dialogView.findViewById(R.id.etComment);
        imgTUP = (ImageButton) dialogView.findViewById(R.id.imgTUP);
        imgTDOWN = (ImageButton) dialogView.findViewById(R.id.imgTDOWN);

        imgTUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strVote = "Good";
            }
        });
        imgTDOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strVote = "Bad";
            }
        });

        // Submit
        btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add new review to database.
                addReview();

                alertDialog.cancel();
                Intent i = new Intent(LineActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        // Skip
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                Intent i = new Intent(LineActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        alertDialog.show();
    }


    private void addReview() {
        // Get user account info.
        mAuth = FirebaseAuth.getInstance();
        String curUID = mAuth.getCurrentUser().getUid();
        databaseReviews = FirebaseDatabase.getInstance().getReference().child("Users").child(curUID);
        final String uemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        databaseReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    final String cname = getIntent().getExtras().getString("cname");
                    final String vcomment = etComment.getText().toString();
                    String uvote = strVote;

                    if (uvote.equals(""))
                    {
                        uvote = "Good";
                    }

                    // Select our database object to work with.
                    databaseReviews= FirebaseDatabase.getInstance().getReference("Reviews");
                    // Add new visit to Firebase DB (i.e. new Visit object).
                    String rid = databaseReviews.push().getKey(); // create a unique id in visits object
                    Review review = new Review(cname, uemail, uvote, vcomment);
                    databaseReviews.child(rid).setValue(review);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

