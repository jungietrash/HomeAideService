package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.homeaide.post.SMSMainActivity;
import com.homeaide.post.chat.ChatMainActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import com.homeaide.post.R;
public class client_booking_details extends AppCompatActivity {



    private ImageView iv_messageCustomer, iv_custPhoto;
    private TextView tv_addressSummary,tv_propertyTypeSummary,tv_brandSummary,tv_acTypeSummary,tv_unitTypeSummary,tv_prefDateSummary,
            tv_prefTimeSummary,tv_contactNumSummary, tv_back, tv_customerName, tv_bookingName, tv_deleteBtn, tv_paymentMethod,
            tv_time, tv_bookingDesc, tv_month, tv_date, tv_day, tv_techContactNumSummary, tv_custAddressSummary, tv_bookPriceSummary, chatus, messageus;
    private Button btn_completeBooking;

    String imageUrl, custID, bookingIdFromIntent, latString, longString, bookingName;

    private FirebaseUser user;
    private FirebaseStorage mStorage;
    private StorageReference projectStorage;
    private DatabaseReference userDatabase, projectDatabase, bookingDatabase;
    private StorageTask addTask;
    private String userID, techID, projName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_booking_details);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        projectStorage = FirebaseStorage.getInstance().getReference("Projects");
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        projectDatabase = FirebaseDatabase.getInstance().getReference("Projects");

        setRef();
        generateBookingData();
        clickListeners();
    }

    private void clickListeners() {

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(client_booking_details.this, my_booking_page.class);
                startActivity(intent);
            }
        });

        tv_deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelBooking();
            }
        });

        chatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(client_booking_details.this, ChatMainActivity.class);
                startActivity(intent);
            }
        });

        messageus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(client_booking_details.this, SMSMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cancelBooking() {
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(client_booking_details.this)
                .setTitle("Cancel Booking?")
                .setMessage("Are you sure you want to cancel this booking?")
                .setCancelable(true)
                .setPositiveButton("Cancel Booking", R.drawable.delete_btn, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        progressDialog = new ProgressDialog(client_booking_details.this);
                        progressDialog.setTitle("Cancelling...");
                        progressDialog.show();

                        bookingIdFromIntent = getIntent().getStringExtra("Booking ID");
                        bookingDatabase.child(bookingIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();

                                }
                                generateNotification();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Back", R.drawable.back_arrow, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }


                })
                .build();

        // Show Dialog
        mBottomSheetDialog.show();


    }

    private void generateNotification() {

        DatabaseReference notificationDB = FirebaseDatabase.getInstance().getReference("Notifications");

        String sp_notifTitle = "Booking cancelled";
        String sp_notifMessage = "Booking: " + bookingName + " has been cancelled by the customer.";

        Date currentTime = Calendar.getInstance().getTime();
        String cartCreated = currentTime.toString();

        Notification notification = new Notification(imageUrl, sp_notifTitle, sp_notifMessage, cartCreated, techID);

        notificationDB.push().setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    Intent intent = new Intent(client_booking_details.this, my_booking_page.class);
                    startActivity(intent);

                    Toast.makeText(client_booking_details.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    private void generateBookingData() {

        bookingIdFromIntent = getIntent().getStringExtra("Booking ID");

        bookingDatabase.child(bookingIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Booking bookingData = snapshot.getValue(Booking.class);

                if(bookingData != null) {
                    try {


                        projName = bookingData.getProjName();
                        techID = bookingData.getTechID();
                        custID  = bookingData.custID;
                        imageUrl = bookingData.imageUrl;
                        bookingName = bookingData.projName;
                        String sp_bookingtime = bookingData.bookingTime;
                        String sp_bookingDate = bookingData.bookingDate;
                        String sp_addInfo = bookingData.addInfo;
                        String sp_address = bookingData.custAddress;
                        String sp_contactNum = bookingData.custContactNum;
                        String sp_propType = bookingData.propertyType;
                        String sp_unitType = bookingData.unitType;
                        String sp_paymentMethod = bookingData.paymentMethod;
                        String sp_price = bookingData.totalPrice;

                        String[] parts = sp_bookingDate.split("/");

                        double price = Double.parseDouble(sp_price);
                        DecimalFormat twoPlaces = new DecimalFormat("0.00");

                        tv_time.setText(sp_bookingtime);
                        tv_month.setText(parts[0]);
                        tv_date.setText(parts[1]);
                        tv_day.setText(parts[3]);

                        tv_bookingName.setText(projName);
                        tv_custAddressSummary.setText(sp_address);
                        tv_contactNumSummary.setText(sp_contactNum);
                        tv_propertyTypeSummary.setText(sp_propType);
                        tv_unitTypeSummary.setText(sp_unitType);

                        tv_bookingDesc.setText(sp_addInfo);
                        tv_paymentMethod.setText(sp_paymentMethod);
                        tv_bookPriceSummary.setText("₱ " + twoPlaces.format(price));

                        generateProfile();
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


        Query query = projectDatabase
                .orderByChild("projName")
                .startAt(projName).endAt(projName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    Projects projects = dataSnapshot.getValue(Projects.class);

                    if(projects.getUserID().equals(techID))
                    {
                        latString = projects.getLatitude();
                        longString = projects.getLongitude();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDatabase.child(techID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (snapshot.exists())
                {

                    tv_techContactNumSummary.setText(users.contactNum);

                    String sp_fName = users.firstName;
                    String sp_lName = users.lastName;
                    String sp_imageUrl = users.imageUrl;
                    String sp_fullName = sp_fName.substring(0, 1).toUpperCase()+ sp_fName.substring(1).toLowerCase()
                            + " " + sp_lName.substring(0, 1).toUpperCase()+ sp_lName.substring(1).toLowerCase();

                    tv_customerName.setText(sp_fullName);

                    if (!sp_imageUrl.isEmpty()) {
                        Picasso.get()
                                .load(sp_imageUrl)
                                .into(iv_custPhoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    private void setRef() {
        iv_messageCustomer = findViewById(R.id.iv_messageCustomer);
        iv_custPhoto = findViewById(R.id.iv_custPhoto);
        tv_paymentMethod = findViewById(R.id.tv_paymentMethod);
        tv_bookPriceSummary = findViewById(R.id.tv_bookPriceSummary);
        chatus = findViewById(R.id.chatUs);
        messageus = findViewById(R.id.messageUs);
        tv_deleteBtn = findViewById(R.id.tv_deleteBtn);
        tv_month = findViewById(R.id.tv_month);
        tv_date = findViewById(R.id.tv_date);
        tv_day = findViewById(R.id.tv_day);
        tv_bookingDesc = findViewById(R.id.tv_bookingDesc);
        tv_time = findViewById(R.id.tv_time);
        tv_back = findViewById(R.id.tv_back);
        tv_bookingName = findViewById(R.id.tv_bookingName);
        tv_customerName = findViewById(R.id.tv_customerName);
        tv_contactNumSummary = findViewById(R.id.tv_contactNumSummary);
        tv_propertyTypeSummary = findViewById(R.id.tv_propertyTypeSummary);
        tv_brandSummary = findViewById(R.id.tv_brandSummary);
        tv_acTypeSummary = findViewById(R.id.tv_acTypeSummary);
        tv_unitTypeSummary = findViewById(R.id.tv_unitTypeSummary);
        tv_prefDateSummary = findViewById(R.id.tv_prefDateSummary);
        tv_prefTimeSummary = findViewById(R.id.tv_prefTimeSummary);
        tv_techContactNumSummary = findViewById(R.id.tv_techContactNumSummary);
        tv_custAddressSummary = findViewById(R.id.tv_custAddressSummary);


        btn_completeBooking = findViewById(R.id.btn_completeBooking);


    }
}