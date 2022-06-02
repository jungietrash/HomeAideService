package com.homeaide.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homeaide.post.Booking.booking.Booking_Add_Service;
import com.homeaide.post.Booking.booking.Admin_Setting;
import com.homeaide.post.Booking.booking.Admin_Search_Page;
import com.homeaide.post.Booking.booking.Booking_Projects;
import com.homeaide.post.chat.ChatMainActivity;

public class HomeActivityForAdmin extends AppCompatActivity {

    private LinearLayout chat,  addService, viewBooking, setting, activeBooking, social, search;

    private TextView notifys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_for_admin);

        chat = findViewById(R.id.chat);
        addService = findViewById(R.id.addService);
        viewBooking = findViewById(R.id.viewBooking);
        setting = findViewById(R.id.setting);
        social = findViewById(R.id.social);
        search = findViewById(R.id.search);
        activeBooking = findViewById(R.id.activeBooking);

        activeBooking = findViewById(R.id.activeBooking);

        activeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, active_booking.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, ChatMainActivity.class);
                startActivity(intent);
            }
        });
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, Booking_Add_Service.class);
                startActivity(intent);
            }
        });
        viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, Booking_Projects.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, Admin_Setting.class);
                startActivity(intent);
            }
        });
        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, RatingActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityForAdmin.this, Admin_Search_Page.class);
                startActivity(intent);
            }
        });

    }
}