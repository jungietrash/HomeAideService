package com.homeaide.post.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.homeaide.post.R;
import com.homeaide.post.RatingActivity;
import com.homeaide.post.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class BookingMainActivity extends AppCompatActivity {
    Fragment homeFragment;
    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_main);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {


                    case R.id.navigation_booking:

                        return true;




                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

    }

    private void loadFragment(Fragment homeFragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.booking_main_menu,menu);
        return true;
    }
}