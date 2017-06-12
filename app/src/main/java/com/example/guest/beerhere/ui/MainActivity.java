package com.example.guest.beerhere.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.beerhere.Constants;
import com.example.guest.beerhere.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mSearchedLocationReference;
    private ValueEventListener mSearchedLocationReferenceListener;


    @Bind(R.id.findBeerButton) Button findBeer;
    @Bind(R.id.aboutButton) Button about;
    @Bind(R.id.editTextLocation) EditText editTextLocation;
    @Bind(R.id.title) TextView title;
    @Bind(R.id.bioButton) Button bio;
    @Bind(R.id.savedBreweriesButton) Button savedBreweriesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);

        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue().toString();
                    Log.d("Location updated", "location: " + location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Typeface beerFont = Typeface.createFromAsset(getAssets(), "fonts/College Block.otf");
        title.setTypeface(beerFont);



        findBeer.setOnClickListener(this);
        about.setOnClickListener(this);
        bio.setOnClickListener(this);
        savedBreweriesButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == findBeer) {  // To the find beer view, which will eventually display nearby beers once api functionality is in place.
            String location = editTextLocation.getText().toString();

            saveLocationToFirebase(location); //calls the methog to save the location zip to firebase.


            Intent intent = new Intent(MainActivity.this, FindBeer.class);
            intent.putExtra("zip", location);
            if (location.length() == 5) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a 5 digit zip code", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == about) {  // To the about page view, just a bit about the app.
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        if (v == bio) {  // To the about page view, just a bit about the app.
            Intent intent = new Intent(MainActivity.this, Bio.class);
            startActivity(intent);
        }

        if (v == savedBreweriesButton) {
            Intent intent = new Intent(MainActivity.this, SavedBreweryListActivity.class);
            startActivity(intent);
        }
    }

    public void saveLocationToFirebase(String location) {
        mSearchedLocationReference.push().setValue(location);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }


    }






