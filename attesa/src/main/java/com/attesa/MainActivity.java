package com.attesa;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;

    ListView clist;




    // *** SIMULATED DATA STRINGS! ** //
        String[] cname = {"Humber Clinic", "Rexdale Clinic", "Family Doctor", "Humber River Walk-in"};
        String[] ctype = {"Walk-in", "Appointment only", "Referral", "Walk-in"};
        Integer[] clive = {2, 5, 11, 12};
        String[] caddr = {"1402 Finch Ave. W", "56 Rexdale Blvd.", "205 Highway 27", "544 Humber Blvd."};
        String cphone = "(000) 000-0000";
        String[] email = {"clinic@gmail.com", "clinic2@gmail.com", "clinic3@gmail.com", "clinic4@gmail.com"};
        String password = "123";
    // *** //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);


        // **** ONLY USED FOR SIMULATED DATA!! ***
        int clinicCount = db.getTableSize("clinics");

        int fakeDataSizeEntries = cname.length;

        for (int i = 0; i < fakeDataSizeEntries; i++)
        {
            Boolean insertFakeClinicData = db.insertClinic(cname[i], ctype[i], cphone, email, password, caddr[i]);
        }



        clist = (ListView)findViewById(R.id.lstClinics);
        ClinicListView clinicListView = new ClinicListView(this, cname, ctype, clive); // What we choose to display per listview item.
        clist.setAdapter(clinicListView);

        clinicSelect();
    }

    private void clinicSelect()
    {
        clist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Item selected: " + cname[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ClinicSelectActivity.class);
                String uHC = getIntent().getExtras().getString("uHC");
                // Pass what we need for selected clinic (i.e. we only pass email since this is a unique string per clinic we can pull up our selected
                // record via the database within the next upcoming activity - ClinicSelectActivity).
                intent.putExtra("cname", cname[position]);
                intent.putExtra("caddr", caddr[position]);
                intent.putExtra("cemail", email[position]);
                intent.putExtra("cLive", clive[position]);
                intent.putExtra("uHC", uHC);

                startActivity(intent);
            }
        });
    }

}
