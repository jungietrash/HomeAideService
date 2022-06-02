package com.homeaide.post;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.homeaide.post.bookingv3.booking.more_page;

public class SMSMainActivity extends AppCompatActivity {

    EditText etPhone,etMessage;
    Button btSend;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity_main);

        etPhone = findViewById(R.id.et_phone);
        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.bt_send);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMSMainActivity.this, more_page.class);
                startActivity(intent);
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(ContextCompat.checkSelfPermission(SMSMainActivity.this
                , Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED){

                sendMessage();
                }else{
                ActivityCompat.requestPermissions(SMSMainActivity.this
                        , new String[]{Manifest.permission.SEND_SMS}
                        ,100);
            }

            }
        });

    }

    private void sendMessage() {
        String sPhone = etPhone.getText().toString().trim();
        String sMessage = etMessage.getText().toString().trim();

        if(!sPhone.equals("") && !sMessage.equals("")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sPhone, null, sMessage, null, null);

            Toast.makeText(getApplicationContext()
                , "SMS sent successfully!", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext()
                ,"Enter value First.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length > 0 && grantResults[0]
            == PackageManager.PERMISSION_GRANTED){
            sendMessage();

        }else{
            Toast.makeText(getApplicationContext()
                    ,"Permission Denied!",Toast.LENGTH_SHORT).show();
        }
    }
}