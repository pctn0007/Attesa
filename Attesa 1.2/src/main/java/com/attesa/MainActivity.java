package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int ONLINE_STATUS = 1;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FirebaseAuth mAuth;

    private DatabaseReference databaseClinics;

    private ListView listViewClinics;

    List<Clinic> clinicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        databaseClinics = FirebaseDatabase.getInstance().getReference("Clinics");

        listViewClinics = (ListView) findViewById(R.id.lstClinics);

        clinicList = new ArrayList<>();


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update list
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseClinics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // clear any existing items.
                clinicList.clear();
                final ClinicList adapter = new ClinicList(MainActivity.this, clinicList);

                listViewClinics.setAdapter(adapter);

                for (DataSnapshot clinicSnapshot : dataSnapshot.getChildren()){
                    Clinic clinic = clinicSnapshot.getValue(Clinic.class);

                    int intStatus = clinic.getCstatus();

                    // Only add to list if clinic is online.
                    if (intStatus == ONLINE_STATUS) {
                        clinicList.add(clinic);
                    }
                }


                // List item selected here:
                listViewClinics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        // Use Clinic to use an adapter to help get the list item position we selected via listview.
                        Clinic itemRef = adapter.getItem(position);

                        Intent i = new Intent(getApplicationContext(), ClinicSelectActivity.class);
                        // everything we need to pass to next activity.
                        i.putExtra("cid", itemRef.getCid());
                        i.putExtra("cname", itemRef.getCname());
                        i.putExtra("ctype", itemRef.getCtype());
                        i.putExtra("cphone", itemRef.getCphone());
                        i.putExtra("clive", itemRef.getClive());
                        i.putExtra("cstatus", itemRef.getCstatus());

                        // go to targeted next activity uppn item click.
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Load user profile.
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_visits) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            Logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout()
    {
        mAuth.signOut();
        mEditor.clear();
        mEditor.commit();
        Intent i = new Intent(MainActivity.this, SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


}
