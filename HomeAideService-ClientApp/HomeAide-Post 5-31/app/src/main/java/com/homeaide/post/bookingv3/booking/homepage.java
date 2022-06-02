package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeaide.post.RatingActivity;
import com.homeaide.post.chat.ChatMainActivity;
import com.squareup.picasso.Picasso;
import com.homeaide.post.R;
public class homepage extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private String userID;

    private Button bookNow, chateau;

    private TextView tv_bannerName,  btn_installation, btn_repair, btn_cleaning, btn_marketplace;

    private EditText et_search;
    private View layout_myOrders, layout_favorites, layout_myCart, layout_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        tv_bannerName = findViewById(R.id.tv_bannerName);
        btn_installation = findViewById(R.id.btn_installation);
        btn_repair = findViewById(R.id.btn_repair);
        btn_cleaning = findViewById(R.id.btn_cleaning);
        btn_marketplace = findViewById(R.id.btn_marketplace);
        bookNow = findViewById(R.id.bookNow);
        chateau = findViewById(R.id.chateau);
        layout_favorites = findViewById(R.id.layout_favorites);

        generateProfile();
        buttonsActivity();
        BottomNavigationView bottomNavigationView;

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(), ChatMainActivity .class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_booking:
                        startActivity(new Intent(getApplicationContext(), my_booking_page.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_setting:
                        startActivity(new Intent(getApplicationContext(), more_page.class));
                        overridePendingTransition(0,0);

                    case R.id.navigation_home:

                        return true;

                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

    }

    private void buttonsActivity() {


        btn_installation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(homepage.this, Handyman_Service_Page.class);
                intent.putExtra("Category", "Handyman");
                intent.putExtra("tabNum", 0);
                startActivity(intent);
            }
        });

        btn_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, Handyman_Service_Page.class);
                intent.putExtra("Category", "Electrical");
                intent.putExtra("tabNum", 1);
                startActivity(intent);
            }
        });

        btn_marketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, Handyman_Service_Page.class);
                intent.putExtra("Category", "Plumbing");
                intent.putExtra("tabNum", 2);
                startActivity(intent);
            }
        });

        btn_cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, Handyman_Service_Page.class);
                intent.putExtra("Category", "Cleaning");
                intent.putExtra("tabNum", 3);
                startActivity(intent);
            }
        });


        layout_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, favorite_page.class);
                startActivity(intent);
            }
        });

        chateau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage.this, ChatMainActivity.class);
                startActivity(intent);
            }
        });

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage.this, search_page.class);
                startActivity(intent);
            }
        });







    }


    private void setRef() {



    }

    private void generateProfile() {

        userDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if(userProfile != null){
                    String sp_fName = userProfile.firstName;
                    String sp_lName = userProfile.lastName;
                    String sp_imageUrl = userProfile.imageUrl;
                    String sp_fullName = sp_fName.substring(0, 1).toUpperCase()+ sp_fName.substring(1).toLowerCase()
                            + " " + sp_lName.substring(0, 1).toUpperCase()+ sp_lName.substring(1).toLowerCase();

                    tv_bannerName.setText(sp_fullName);

                    if (!sp_imageUrl.isEmpty()) {
                        Picasso.get()
                                .load(sp_imageUrl);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(homepage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}