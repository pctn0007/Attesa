package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ClinicSelectActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Button btnCheckIn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseClinics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_select);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        final String cname = getIntent().getExtras().getString("cname");
        String clive = getIntent().getExtras().getString("clive");
        String ctype = getIntent().getExtras().getString("ctype");
        String cstatus = getIntent().getExtras().getString("cstatus");
        final String cphone = getIntent().getExtras().getString("cphone");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        databaseClinics = FirebaseDatabase.getInstance().getReference("Reviews");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Confirmation to send sensitive data (e.g. health card no.).
                showConfirmDialog(cname);
            }
        });

    }

    private void showConfirmDialog(String clinicName)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.inline_confirm, null);
        dialogBuilder.setView(dialogView);

        final TextView tvConfirm = (TextView) dialogView.findViewById(R.id.tvConfirm);
        final Button btnConfirm = (Button) dialogView.findViewById(R.id.btnConfirm);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // Add new visit
            addVisit();

            // Toast.makeText(getApplicationContext(), R.string.str_visitConfirmedMsg, Toast.LENGTH_LONG).show();
            Intent i = new Intent(ClinicSelectActivity.this, LineActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            }
        });

    }


    private void addVisit() {

        // Get user account info.
        mAuth = FirebaseAuth.getInstance();
        String curUID = mAuth.getCurrentUser().getUid();
        databaseClinics = FirebaseDatabase.getInstance().getReference().child("Users").child(curUID);

        databaseClinics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    final String uhc = dataSnapshot.child("HC").getValue().toString();

                    final String myHC = uhc;
                    final String cname = getIntent().getExtras().getString("cname");
                    final String uvote = "Good"; // every visit starts with a Good rating (we modify if needed post-visit confirmation).

                    // Error checking
                    if (myHC.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.str_errHC, Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Select our database object to work with.
                    databaseClinics = FirebaseDatabase.getInstance().getReference("Visits");
                    // Add new visit to Firebase DB (i.e. new Visit object).
                    if (!TextUtils.isEmpty(myHC)){
                        String vid = databaseClinics.push().getKey(); // create a unique id in visits object
                        Visit visit = new Visit(cname, myHC, uvote);
                        databaseClinics.child(vid).setValue(visit);

                        // Update user account status.
                        databaseClinics = FirebaseDatabase.getInstance().getReference("Users");
                        String cid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String dir = databaseClinics.child(cid).child("ustatus").getKey();
                        databaseClinics.child(cid).child(dir).setValue(1);


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), R.string.str_enter_clinic_name, Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        RelativeLayout tabDetailsContent;
        RelativeLayout tabReviewsContent;

        private ListView listViewReviews;
        private List<Review> reviewList;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, String cname, String clive) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("cname", cname);
            args.putString("clive", clive);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_clinic_select, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            tabDetailsContent = (RelativeLayout) rootView.findViewById(R.id.tab1_content);
            tabReviewsContent = (RelativeLayout) rootView.findViewById(R.id.tab2_content);

            tabDetailsContent.setVisibility(View.VISIBLE);
            tabReviewsContent.setVisibility(View.GONE);

            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            FirebaseAuth mAuth;
            DatabaseReference databaseClinic;

            int i = getArguments().getInt(ARG_SECTION_NUMBER) - 1; // subtract one since out args start at 0 and tab starts at 1 (must match for eg. 1:1 or 0:0).

            switch(i)
            {
                // Tab 1 content
                default:
                case 0:

                    tabDetailsContent.setVisibility(View.VISIBLE);
                    tabReviewsContent.setVisibility(View.GONE);

                    String cname = getArguments().getString("cname");
                    String clive = getArguments().getString("clive");

                    // @Note: parse the string version of clive via DB so we can do perform math functions on it.
                        int iparse = Integer.parseInt(clive);
                    //

                    textView.setText(String.valueOf(iparse));

                break;

                // Tab 2 content
                case 1:

                    tabDetailsContent.setVisibility(View.GONE);
                    tabReviewsContent.setVisibility(View.VISIBLE);



                break;

            }


            return rootView;
        }
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final String cname = getIntent().getExtras().getString("cname");
            final String clive = getIntent().getExtras().getString("clive");

            return PlaceholderFragment.newInstance(position + 1, cname, clive);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }



}
