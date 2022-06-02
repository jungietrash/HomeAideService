package com.homeaide.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homeaide.post.Booking.adaptorsfragments.fragmentAdapter2;

public class active_booking extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private String userID;

    private ViewPager2 viewPager;
    private fragmentAdapter2 adapter;

    private ImageView backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_booking);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        backBTN = findViewById(R.id.backBTN);
        viewPager = findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new fragmentAdapter2(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(active_booking.this, HomeActivityForAdmin.class);
                startActivity(intent);
            }
        });

    }
}