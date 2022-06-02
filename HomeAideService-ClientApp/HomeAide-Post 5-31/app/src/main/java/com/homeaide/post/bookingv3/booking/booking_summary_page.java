package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;
import com.homeaide.post.MainActivity;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.booking.BookingLoginActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.homeaide.post.R;

import org.json.JSONObject;

public class booking_summary_page extends AppCompatActivity implements PaymentResultListener {

    private FirebaseUser user;
    private DatabaseReference projDatabase, userData, bookingDatabase;
    private StorageTask addTask;
    private String userID;
    LoginDetails lg = new LoginDetails();

    private TextView tv_addressSummary,tv_propertyTypeSummary,tv_unitTypeSummary,tv_prefDateSummary,
            tv_prefTimeSummary,tv_contactNumSummary,tv_addInfoSummary,tv_projNameSummary,
            tv_bookPriceSummry,tv_jobPriceSummary, tv_back, tv_bookPricecharge;
    private ProgressBar progressBar;
    private ImageView iv_projPhotoSummary;
    private Button btn_confirmBooking;
    private RadioGroup radioGroup;
    private RadioButton rb_gcash, rb_cod;
    private String totalPrice, imageUrl, bookingCreated, paymentMethod, custID, sp_techId , projectIdFromIntent, addressFromIntent, propertyTypeFromIntent,
            airconBrandFromIntent, airconTypeFromIntent, unitTypeFromIntent, bookingDateFromIntent, bookingTimeFromIntent,
            contactNumberFromIntent, addInfoFromIntent, projName, latString, longString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_summary_page);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        projDatabase = FirebaseDatabase.getInstance().getReference("Projects");

        setRef();
        generateData();
        clickListeners();
    }

    private void clickListeners() {

        rb_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.check(R.id.rb_cod);
                paymentMethod = "COD";

            }
        });

        rb_gcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.check(R.id.rb_gcash);
                paymentMethod = "RazorPay";

            }
        });

        btn_confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    new SweetAlertDialog(
                            booking_summary_page.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("Please choose payment!")
                            .show();
                }
                else
                {
                    new SweetAlertDialog(booking_summary_page.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning!.")
                            .setCancelText("Cancel")
                            .setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {


                                    if(paymentMethod.equals("RazorPay")){
                                        makepayment();
                                    }
                                   else if(paymentMethod.equals("COD")){
                                        submitBooking();
                                        startActivity(new Intent(booking_summary_page.this, my_booking_page.class));
                                    }
                                   else{
                                        Toast.makeText(booking_summary_page.this, "Select Payment Method", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setContentText("Please make sure \nall information \nare correct!")
                            .show();

                }


            }
        });

    }

    private void makepayment()
    {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_0glkg1PBfdw8m7");

        checkout.setImage(R.drawable.homeaide_logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", lg.getProjName());
            options.put("description", lg.getProjAddress());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#FEC05A");
            options.put("currency", "USD");
            options.put("amount", lg.getProjPriceConverted() * 100);//300 X 100 == 300.00
            options.put("prefill.email", "homeaideservice@gmail.com");
            options.put("prefill.contact","7617046325"); //number predefined due to philippines not included in country lists
            checkout.open(activity, options);



        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    private void setRef() {

        tv_addressSummary = findViewById(R.id.tv_addressSummary);
        tv_propertyTypeSummary = findViewById(R.id.tv_propertyTypeSummary);

        tv_unitTypeSummary = findViewById(R.id.tv_unitTypeSummary);
        tv_prefDateSummary = findViewById(R.id.tv_prefDateSummary);
        tv_prefTimeSummary = findViewById(R.id.tv_prefTimeSummary);
        tv_contactNumSummary = findViewById(R.id.tv_contactNumSummary);
        tv_addInfoSummary = findViewById(R.id.tv_addInfoSummary);
        tv_projNameSummary = findViewById(R.id.tv_projNameSummary);
        tv_bookPricecharge = findViewById(R.id.tv_bookPricecharge);

        tv_bookPriceSummry = findViewById(R.id.tv_bookPriceSummry);
        tv_jobPriceSummary = findViewById(R.id.tv_jobPriceSummary);
        tv_back = findViewById(R.id.tv_back);

        rb_gcash = findViewById(R.id.rb_gcash);
        rb_cod = findViewById(R.id.rb_cod);
        radioGroup = findViewById(R.id.radioGroup);

        progressBar = findViewById(R.id.progressBar);

        iv_projPhotoSummary = findViewById(R.id.iv_projPhotoSummary);

        btn_confirmBooking = findViewById(R.id.btn_completeBooking);
    }

    private void generateData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        latString = extras.getString("latitude");
        longString = extras.getString("longitude");
        projectIdFromIntent = extras.getString("projectIdFromIntent");
        addressFromIntent = extras.getString("address");
        propertyTypeFromIntent = extras.getString("property type");
        
        unitTypeFromIntent = extras.getString("unit type");
        bookingDateFromIntent = extras.getString("booking date");
        bookingTimeFromIntent = extras.getString("booking time");
        contactNumberFromIntent = extras.getString("contact number");
        addInfoFromIntent = extras.getString("add info");

        projDatabase.child(projectIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Projects projData = snapshot.getValue(Projects.class);

                if(projData != null) {

                    imageUrl = projData.getImageUrl();
                    custID = userID;
                    sp_techId = projData.userID;
                    projName = projData.projName;
                    String sp_techLocation = projData.projAddress;
                    String sp_projPrice = projData.price;


                    lg.setProjName(projName);

                    lg.setProjAddress(sp_techLocation);

                    tv_addressSummary.setText(addressFromIntent);
                    tv_propertyTypeSummary.setText(propertyTypeFromIntent);

                    tv_unitTypeSummary.setText(unitTypeFromIntent);
                    tv_prefDateSummary.setText(bookingDateFromIntent);
                    tv_prefTimeSummary.setText(bookingTimeFromIntent);
                    tv_contactNumSummary.setText(contactNumberFromIntent);
                    tv_addInfoSummary.setText(addInfoFromIntent);

                    totalPrice = sp_projPrice;
                    double price = Double.parseDouble(sp_projPrice);
                    double charge = Double.parseDouble(sp_projPrice) * .15;
                    double total = price+charge;
                    DecimalFormat twoPlaces = new DecimalFormat("0.00");

                    tv_projNameSummary.setText(projName);

                    tv_bookPriceSummry.setText("₱ " + twoPlaces.format(price));
                    tv_bookPricecharge.setText("₱ " + twoPlaces.format(charge));
                    tv_jobPriceSummary.setText("₱ " + twoPlaces.format(total));

                    lg.setProjPrice(String.valueOf(total));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void submitBooking() {
        final ProgressDialog progressDialog = new ProgressDialog(booking_summary_page.this);
        progressDialog.setTitle("Processing your booking...");
        progressDialog.show();

        Date currentTime = new Date();

        bookingCreated = currentTime.toString();
        Booking booking = new Booking(imageUrl, custID, projName, addressFromIntent, latString, longString, propertyTypeFromIntent, airconBrandFromIntent,
                airconTypeFromIntent, unitTypeFromIntent, bookingDateFromIntent, bookingTimeFromIntent,
                contactNumberFromIntent, addInfoFromIntent, totalPrice, paymentMethod, sp_techId, bookingCreated);

        bookingDatabase.push().setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(booking_summary_page.this, "Success", Toast.LENGTH_SHORT).show();



                } else {
                    Toast.makeText(booking_summary_page.this, "Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Booked Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(booking_summary_page.this, my_booking_page.class);
        startActivity(intent);


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Booked Failed", Toast.LENGTH_SHORT).show();
    }
}