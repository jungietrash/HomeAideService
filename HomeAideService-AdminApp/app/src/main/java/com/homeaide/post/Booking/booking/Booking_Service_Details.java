package com.homeaide.post.Booking.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.homeaide.post.Booking.adaptorsfragments.PhonenumberGetter;
import com.homeaide.post.chat.UsersActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import com.homeaide.post.R;
import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;

public class Booking_Service_Details extends AppCompatActivity {


    EditText etPhone,etMessage;
    Button btSend;


    private ImageView bookingPhoto, backBTN;
    private TextView tv_addressSummary,tv_propertyTypeSummary,dprice,tv_unitTypeSummary,tv_contactNumSummary, tv_customerName, tv_bookingName,
            tv_time, tv_specialInstruction, tv_month, tv_date, tv_day;
    private ProgressBar progressBar;
    private Button btn_completeBooking, iv_messageCustomer,message;


    String imageUrl, custID, bookingIdFromIntent, latString, longString, sp_contactNum;

    private FirebaseUser user;
    private FirebaseStorage mStorage;
    private StorageReference projectStorage;
    private DatabaseReference userDatabase, projectDatabase, bookingDatabase;
    private StorageTask addTask;
    private String userID;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_details);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        projectStorage = FirebaseStorage.getInstance().getReference("Services");
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        projectDatabase = FirebaseDatabase.getInstance().getReference("Services");

        setRef();
        clickListener();
        generateBookingData();

        etPhone = findViewById(R.id.et_phone);

        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.bt_send);


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Booking_Service_Details.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){

                    sendMessage();
                }else{
                    ActivityCompat.requestPermissions(Booking_Service_Details.this
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



    private void clickListener() {

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_messageCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booking_Service_Details.this, UsersActivity.class);
                startActivity(intent);
            }
        });

        btn_completeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmBooking();
            }
        });


    }

    private void confirmBooking() {
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(Booking_Service_Details.this)
                .setTitle("Confirm Booking?")
                .setCancelable(true)
                .setPositiveButton("Finished Service", R.drawable.delete_btn, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        progressDialog = new ProgressDialog(Booking_Service_Details.this);
                        progressDialog.show();

                        bookingIdFromIntent = getIntent().getStringExtra("Booking ID");
                        bookingDatabase.child(bookingIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Back", R.drawable.backbtn, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mBottomSheetDialog.show();

    }

PhonenumberGetter png = new PhonenumberGetter();

    private void generateBookingData() {

        bookingIdFromIntent = getIntent().getStringExtra("Booking ID");

        bookingDatabase.child(bookingIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Booking bookingData = snapshot.getValue(Booking.class);

                if(bookingData != null) {
                    try {
                        latString = bookingData.getLatitude();
                        longString = bookingData.getLongitude();

                        custID  = bookingData.custID;
                        imageUrl = bookingData.imageUrl;
                        String sp_bookingName = bookingData.projName;
                        String sp_bookingtime = bookingData.bookingTime;
                        String sp_bookingDate = bookingData.bookingDate;
                        String sp_addInfo = bookingData.addInfo;
                        String sp_address = bookingData.custAddress;
                        sp_contactNum = bookingData.custContactNum;
                        etPhone.setText(sp_contactNum);
                        String sp_propType = bookingData.propertyType;
                        String sp_unitType = bookingData.unitType;
                        String sp_price = bookingData.totalPrice;

                        String[] parts = sp_bookingDate.split("/");

                        double price = Double.parseDouble(sp_price);
                        DecimalFormat twoPlaces = new DecimalFormat("0.00");

                        Picasso.get().load(imageUrl).into(bookingPhoto);
                        tv_bookingName.setText(sp_bookingName);
                        tv_time.setText(sp_bookingtime);
                        tv_month.setText(parts[0]);
                        tv_date.setText(parts[1]);
                        tv_day.setText(parts[3]);
                        tv_specialInstruction.setText(sp_addInfo);

                        tv_addressSummary.setText(sp_address);
                        tv_contactNumSummary.setText(sp_contactNum);
                        png.setNumber(sp_contactNum);
                        tv_propertyTypeSummary.setText(sp_propType);
                        tv_unitTypeSummary.setText(sp_unitType);
                        dprice.setText("Total Price: ₱ " + twoPlaces.format(price));

                        generateProfile();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateProfile() {
        userDatabase.child(custID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users custData = snapshot.getValue(Users.class);
                if(snapshot.exists()){

                    String sp_fName = custData.firstName;
                    String sp_lName = custData.lastName;
                    String sp_imageUrl = custData.imageUrl;
                    String sp_fullName = sp_fName.substring(0, 1).toUpperCase()+ sp_fName.substring(1).toLowerCase()
                            + " " + sp_lName.substring(0, 1).toUpperCase()+ sp_lName.substring(1).toLowerCase();

                    tv_customerName.setText(sp_fullName);
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Booking_Service_Details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRef() {
        bookingPhoto = findViewById(R.id.bookingPhoto);
        iv_messageCustomer = findViewById(R.id.iv_messageCustomer);

        dprice = findViewById(R.id.price);
        tv_month = findViewById(R.id.tv_month);
        tv_date = findViewById(R.id.tv_date);
        tv_day = findViewById(R.id.tv_day);
        tv_specialInstruction = findViewById(R.id.tv_specialInstruction);
        tv_time = findViewById(R.id.tv_time);
        message = findViewById(R.id.message);
        backBTN = findViewById(R.id.backBTN);
        tv_bookingName = findViewById(R.id.tv_bookingName);
        tv_customerName = findViewById(R.id.tv_customerName);
        tv_addressSummary = findViewById(R.id.tv_addressSummary);
        tv_contactNumSummary = findViewById(R.id.tv_contactNumSummary);
        tv_propertyTypeSummary = findViewById(R.id.tv_propertyTypeSummary);
        tv_unitTypeSummary = findViewById(R.id.tv_unitTypeSummary);

        progressBar = findViewById(R.id.progressBar);

        btn_completeBooking = findViewById(R.id.btn_completeBooking);


    }



}