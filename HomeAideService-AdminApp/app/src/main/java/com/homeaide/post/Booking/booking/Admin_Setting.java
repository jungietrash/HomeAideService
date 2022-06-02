package com.homeaide.post.Booking.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeaide.post.HomeActivityForAdmin;
import com.homeaide.post.utilities.PreferenceManager;
import com.squareup.picasso.Picasso;

import com.homeaide.post.R;

public class Admin_Setting extends AppCompatActivity {
    private PreferenceManager preferenceManager;

    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private String userID;

    private ImageView profileImage1, backBTN;
    private Button logoutBTN;
    private TextView editProfile,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_setting);

        profileImage1 = findViewById(R.id.profileImage1);
        editProfile = findViewById(R.id.editProfile);
        logoutBTN = findViewById(R.id.logoutBTN);
        userName = findViewById(R.id.userName);
        backBTN = findViewById(R.id.backBTN);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Setting.this, Admin_Profile_Page.class);
                startActivity(intent);
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                logout1();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), sign_in.class));
                finish();
            }

        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Admin_Setting.this, HomeActivityForAdmin.class);
                startActivity(intent);

            }
        });

        AdminProfile();

    }

    private void logout1(){

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();

    }

    private void AdminProfile() {

        userDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if(userProfile != null){
                    String firstName = userProfile.firstName;
                    String lastName = userProfile.lastName;
                    String profileImage = userProfile.imageUrl;
                    String completeName = firstName.substring(0, 1).toUpperCase()+ firstName.substring(1).toLowerCase() + " " + lastName.substring(0, 1).toUpperCase()+ lastName.substring(1).toLowerCase();

                    userName.setText(completeName);

                    try{
                        if (!profileImage.isEmpty()) {
                            Picasso.get()
                                    .load(profileImage)
                                    .into(profileImage1);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Setting.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}