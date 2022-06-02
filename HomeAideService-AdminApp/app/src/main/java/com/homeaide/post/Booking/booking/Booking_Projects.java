package com.homeaide.post.Booking.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.homeaide.post.HomeActivityForAdmin;
import com.homeaide.post.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.homeaide.post.Booking.adaptorsfragments.fragmentAdapter;

public class Booking_Projects extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private String userID;
    private ViewPager2 vp_viewPager2;
    private fragmentAdapter adapter;
    private ImageView backBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_services);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        backBTN = findViewById(R.id.backBTN);
        vp_viewPager2 = findViewById(R.id.vp_viewPager2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new fragmentAdapter(fragmentManager, getLifecycle());
        vp_viewPager2.setAdapter(adapter);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booking_Projects.this, HomeActivityForAdmin.class);
                startActivity(intent);
            }
        });
    }
}