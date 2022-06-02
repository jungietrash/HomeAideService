package com.homeaide.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.homeaide.post.bookingv3.booking.login_page;
import com.homeaide.post.bookingv3.booking.more_page;
import com.homeaide.post.bookingv3.booking.sign_up_page;

public class Setting_Terms_and_Condition extends AppCompatActivity {

    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_terms_and_condition);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_Terms_and_Condition.this, more_page.class);
                startActivity(intent);
            }
        });

    }

}