package com.example.testing01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.translation.TranslationRequest;
import android.widget.Button;
import android.widget.ImageView;

import com.example.testing01.Login_as;
import com.example.testing01.R;
import com.example.testing01.Setting_Logout;
import com.example.testing01.User_Login;
import com.example.testing01.activities.Technician_Booking;
import com.example.testing01.activities.Technician_Chat;
import com.example.testing01.activities.Technician_Profile;
import com.example.testing01.activities.Technician_Setting;
import com.google.firebase.auth.FirebaseAuth;


public class Technician_LandingPage extends AppCompatActivity {

    ImageView viewProfile, viewBooking, viewSetting, viewChat;
    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_landing_page);

        viewProfile = findViewById(R.id.viewProfile);
        viewBooking = findViewById(R.id.viewBooking);
        viewSetting = findViewById(R.id.viewSetting);
        viewChat = findViewById(R.id.viewChat);

        ImageView viewProfile = (ImageView) findViewById(R.id.viewProfile);
        ImageView viewBooking = (ImageView) findViewById(R.id.viewBooking);
        ImageView viewSetting = (ImageView) findViewById(R.id.viewSetting);
        ImageView viewChat = (ImageView) findViewById(R.id.viewChat);
        Button signOutBtn = findViewById(R.id.signout);


        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Profile.class));
            }
        });
        viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Booking.class));
            }
        });
        viewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Setting.class));
            }
        });
        viewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Chat.class));
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login_as.class));

            }
        });

    }
}