package com.attesa;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClinicSelectActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    DatabaseHelper db;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_select);

        db = new DatabaseHelper(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Change title (custom displays clinic name above tabs)
        final String cname = getIntent().getExtras().getString("cname");
        final String uHC = getIntent().getExtras().getString("uHC");

        TextView selectedTitle = (TextView)findViewById(R.id.tvSelectedTitle);
        selectedTitle.setText(cname);


        Button btnCheckIn = (Button)findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sending patient details over to clinic now...", Toast.LENGTH_SHORT).show();
                // Add visit to database.
                String userHC = uHC;
                System.out.println(userHC);
                Boolean addVisit = db.insertVisit(userHC, cname, 1); // status: 1 - visit is ongoing; 0 - visit is completed
                if (addVisit)
                {
                    System.out.println("INSERTED NEW VISIT INTO DB!!!!!");
                }
            }
        });

    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String PASS_CADDR = "passed_caddr";
        private static final String PASS_CNAME = "passed_cname";
        private static final String PASS_CLIVE = "passed_clive";
        private static final String PASS_UHC = "uHC";

      private View fragmentView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String sectionAddr, String sectionLive, String sectionHC) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(PASS_CADDR, sectionAddr);
            args.putString(PASS_CLIVE, sectionLive);
            args.putString(PASS_UHC, sectionHC);

            fragment.setArguments(args);
            return fragment;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            String caddr = getArguments().getString(PASS_CNAME);
            String clive = getArguments().getString(PASS_CLIVE);
            String uHC = getArguments().getString(PASS_UHC);

            switch(getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    fragmentView = inflater.inflate(R.layout.fragment_clinic_profile, container, false);

                    // Set address in GUI
                    TextView textAddr = (TextView) fragmentView.findViewById(R.id.txtcaddr);
                    textAddr.setText(caddr);

                    // Set GUI and text for People ahead value/number.
                    TextView textLive = (TextView) fragmentView.findViewById(R.id.txtcpplcount);
                    textAddr.setText(clive);


                break;
                case 2:
                    fragmentView = inflater.inflate(R.layout.fragment_clinic_visits, container, false);
                    TextView textView = (TextView) fragmentView.findViewById(R.id.txtcvisits_title);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                break;

            }

            return fragmentView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        String caddr = db.getcaddr(getIntent().getExtras().getString("caddr"));
        String clive = getIntent().getExtras().getString("cLive");
        String uHC = getIntent().getExtras().getString("cHC");

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, caddr, clive, uHC);
        }

        @Override
        public int getCount() {

            return 2;
        }
    }
}
