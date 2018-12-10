package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LineActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000; // 10 minutes

    private Button btnCancel;
    private TextView tvTimer;

    private CountDownTimer mCounterdown;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        tvTimer = (TextView) findViewById(R.id.tvTimer);

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
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

                // do something when time reaches zero!
                Intent i = new Intent(LineActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText()
    {
        int minutes = (int)( mTimeLeftInMillis / 1000) / 60;
        int seconds = (int)( mTimeLeftInMillis / 1000) % 60;

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

}

