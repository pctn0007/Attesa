package com.attesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ReviewActivity extends AppCompatActivity {


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReviews;

    private ListView listViewReviews;
    List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        mAuth = FirebaseAuth.getInstance();
        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews");

        listViewReviews = (ListView) findViewById(R.id.lstReviews);
        reviewList = new ArrayList<>();

    }



    @Override
    protected void onStart() {
        super.onStart();

        databaseReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // clear any existing items.
                reviewList.clear();
                final ReviewList adapter = new ReviewList(ReviewActivity.this, reviewList);

                listViewReviews.setAdapter(adapter);

                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()){
                    Review review = reviewSnapshot.getValue(Review.class);

                    String strUEmail = review.getUemail();
                    String getUEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    // Only add to list if the review is associated with user logged in.
                    if (getUEmail.equals(strUEmail)) {

                        reviewList.add(review);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
