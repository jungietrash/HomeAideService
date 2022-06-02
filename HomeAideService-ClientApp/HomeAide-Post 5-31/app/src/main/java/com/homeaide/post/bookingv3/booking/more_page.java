package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.AboutUs;
import com.homeaide.post.RatingActivity;
import com.homeaide.post.SMSMainActivity;
import com.homeaide.post.SetUpActivity;
import com.homeaide.post.Setting_Terms_and_Condition;
import com.homeaide.post.chat.ChatMainActivity;
import com.homeaide.post.utilities.Constants;
import com.homeaide.post.utilities.PreferenceManager;
import com.squareup.picasso.Picasso;

import com.homeaide.post.R;

import java.util.HashMap;

public class more_page extends AppCompatActivity {
    private PreferenceManager preferenceManager;

    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private String userID;

    private ImageView userPhoto;
    private Button termsandconditionss;
    private TextView editProfile,bannerName,back;

    private Button contactUs, aboutUs, logout, Nickname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_page);


        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        userPhoto = findViewById(R.id.iv_userPhoto);

        editProfile = findViewById(R.id.tv_editProfile);
        contactUs = findViewById(R.id.tv_contactUs);
        aboutUs = findViewById(R.id.tv_aboutUs);
        logout = findViewById(R.id.tv_logout);
        bannerName = findViewById(R.id.tv_bannerName);
        back = findViewById(R.id.tv_back);
        termsandconditionss = findViewById(R.id.termsandconditionss);
        Nickname = findViewById(R.id.Nickname);


        clickListeners();
        generateProfile();

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_setting);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(), ChatMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_booking:
                        startActivity(new Intent(getApplicationContext(), my_booking_page.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_setting:


                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), homepage.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    private void clickListeners() {

        termsandconditionss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(more_page.this, Setting_Terms_and_Condition.class);
                startActivity(intentEditProfile);
            }
        }); // end of edit profile button

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(more_page.this, edit_profile_page.class);
                startActivity(intentEditProfile);
            }
        }); // end of edit profile button

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(more_page.this, SMSMainActivity.class);
                startActivity(intentEditProfile);
            }
        }); // end of edit profile button

        Nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(more_page.this, SetUpActivity.class);
                startActivity(intentEditProfile);
            }
        }); // end of edit profile button


        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(more_page.this, AboutUs.class);
                startActivity(intentEditProfile);
            }
        }); // end of edit profile button




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout1();
               logout2();
               Intent intentEditProfile = new Intent(more_page.this, login_page.class);
               startActivity(intentEditProfile);


            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void logout1() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login_page.class));
        finish();
    }

    private void logout2() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    finish();
                }).addOnFailureListener(e -> showToast("Error"));

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login_page.class));
        finish();
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

                    bannerName.setText(sp_fullName);

                    try{
                        if (!sp_imageUrl.isEmpty()) {
                            Picasso.get()
                                    .load(sp_imageUrl)
                                    .into(userPhoto);
                        }

                    }
                    catch (Exception e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(more_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

}