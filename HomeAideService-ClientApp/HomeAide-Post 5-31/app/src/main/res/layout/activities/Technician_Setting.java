package com.example.testing01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testing01.R;

public class Technician_Setting extends AppCompatActivity {

    TextView profile,aboutus,termsandconditionss,signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_setting);


        TextView profile = findViewById(R.id.profile);
        TextView aboutus = findViewById(R.id.aboutus);
        TextView termsandconditionss = findViewById(R.id.termsandconditionss);
        TextView signout = findViewById(R.id.signout);




        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Profile.class));
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_About_Us.class));
            }
        });

        termsandconditionss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Terms_and_Condition.class));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Technician_Logout.class));
            }
        });
    }
}