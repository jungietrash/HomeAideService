package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.homeaide.post.R;
public class booking_page extends AppCompatActivity {

    private TextView chip_Mon, chip_Tue, chip_Wed, chip_Thu, chip_Fri, chip_Sat, chip_Sun;
    private ImageView iv_projectImage,  backBtn;
    private TextView tv_projName, tv_projRating, tv_projPrice, tv_back, tv_projDesc, tv_availabilityText, tv_timeAvailable, tv_quantity;
    private Button btn_bookNow, btn_orderNow, iv_favorite;
    private View layout_favorite, layout_cart;

    private FirebaseUser user;
    private DatabaseReference projectDatabase, listingDatabase, favoriteDatabase, cartDatabase;
    private String userID, projectIdFromIntent, listingIdFromIntent, imageUrlText, latLng,  tempProjectID, tempProjName,
            tempProjPrice, tempProjRatings, tempListName, listPrice, listRatings;
    private Uri tempUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_page);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        StorageReference projectStorage = FirebaseStorage.getInstance().getReference("Projects");
        projectDatabase = FirebaseDatabase.getInstance().getReference("Projects");
        listingDatabase = FirebaseDatabase.getInstance().getReference("Listings");
        favoriteDatabase = FirebaseDatabase.getInstance().getReference("Favorites");
        cartDatabase = FirebaseDatabase.getInstance().getReference("Cart");

        listingIdFromIntent = getIntent().getStringExtra("Listing ID");
        projectIdFromIntent = getIntent().getStringExtra("Project ID");
        backBtn = findViewById(R.id.backBtn);
        chip_Mon = findViewById(R.id.monday);
        chip_Tue = findViewById(R.id.tuesday);
        chip_Wed = findViewById(R.id.wednesday);
        chip_Thu = findViewById(R.id.thursday);
        chip_Fri = findViewById(R.id.friday);
        chip_Sat = findViewById(R.id.saturday);
        chip_Sun = findViewById(R.id.sunday);

        setRef();
        generateProjDataValue();
        clickListeners();
    }

    private void clickListeners() {

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getKey();
                Intent intent = new Intent(booking_page.this, Handyman_Service_Page.class);
                startActivity(intent);
            }
        });

            iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addToFavorite();

            }
        });

        btn_bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getKey();
                Intent intent = new Intent(booking_page.this, booking_application_page.class);
                intent.putExtra("projectIdFromIntent", tempProjectID);
                startActivity(intent);
            }
        });

    }

    private void addToFavorite() {

        favoriteDatabase
                .orderByChild("projName")
                .startAt(tempProjName).endAt(tempProjName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Favorites f = dataSnapshot.getValue(Favorites.class);
                        if( f.getCustID().equals(userID) )
                        {
                            Intent intent = new Intent(booking_page.this, favorite_page.class);
                            startActivity(intent);
                        }
                    }


                }
                else
                {
                    //Project ID doesn't exists.
                    Date currentTime = Calendar.getInstance().getTime();
                    String favoriteCreated = currentTime.toString();
                    Favorites favorites = new Favorites(userID, projectIdFromIntent, favoriteCreated, imageUrlText, tempProjName, tempProjPrice, tempProjRatings);

                    favoriteDatabase.push().setValue(favorites).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new SweetAlertDialog(booking_page.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Subscribed")
                                        .show();


                            } else {
                                Toast.makeText(booking_page.this, "Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setRef() {

        iv_projectImage = findViewById(R.id.iv_projPhotoSummary);

        tv_projRating = findViewById(R.id.tv_projRating);
        iv_favorite = findViewById(R.id.iv_favorite);

        tv_projRating = findViewById(R.id.tv_projRating);
        tv_projName = findViewById(R.id.tv_projName);
        tv_projPrice = findViewById(R.id.tv_projPrice);
        tv_projDesc = findViewById(R.id.tv_projDesc);
        tv_back = findViewById(R.id.tv_back);
        tv_availabilityText = findViewById(R.id.tv_availabilityText);
        tv_timeAvailable = findViewById(R.id.tv_timeAvailable);
        tv_quantity = findViewById(R.id.tv_quantity);

        btn_bookNow = findViewById(R.id.btn_bookNow);
        btn_orderNow = findViewById(R.id.btn_orderNow);





    }

    private void generateProjDataValue() {

        projectDatabase.child(projectIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Projects projectData = snapshot.getValue(Projects.class);

                if(projectData != null){
                    try{
                        tempProjectID = projectIdFromIntent;

                        imageUrlText = projectData.getImageUrl();
                        tempProjName = projectData.getProjName();
                        String sp_projPrice = projectData.getPrice();
                        String sp_projSpecialInstruction = projectData.getProjInstruction();
                        String sp_startTime = projectData.getStartTime();
                        String sp_endTime = projectData.getEndTime();
                        tempProjRatings = projectData.getRatings();

                        boolean sp_isAvailableMon = projectData.isAvailableMon();
                        boolean sp_isAvailableTue = projectData.isAvailableTue();
                        boolean sp_isAvailableWed = projectData.isAvailableWed();
                        boolean sp_isAvailableThu = projectData.isAvailableThu();
                        boolean sp_isAvailableFri = projectData.isAvailableFri();
                        boolean sp_isAvailableSat = projectData.isAvailableSat();
                        boolean sp_isAvailableSun = projectData.isAvailableSun();


                        tempUri = Uri.parse(imageUrlText);


                        Picasso.get()
                                .load(tempUri)
                                .resize(800, 600)
                                .into(iv_projectImage);

                        double price = Double.parseDouble(sp_projPrice);
                        DecimalFormat twoPlaces = new DecimalFormat("0.00");
                        tempProjPrice = twoPlaces.format(price);

                        tv_projName.setText(tempProjName);
                        tv_projPrice.setText(" ₱ " + tempProjPrice);
                        tv_timeAvailable.setText(sp_startTime + " - " + sp_endTime);
                        tv_projDesc.setText(sp_projSpecialInstruction);

                        if(sp_isAvailableMon == true){
                            chip_Mon.setVisibility(View.VISIBLE);
                            chip_Mon.setAllCaps(true);
                            chip_Mon.setClickable(false);
                        }
                        if(sp_isAvailableTue == true){
                            chip_Tue.setVisibility(View.VISIBLE);
                            chip_Tue.setAllCaps(true);
                            chip_Tue.setClickable(false);
                        }
                        if(sp_isAvailableWed == true){
                            chip_Wed.setVisibility(View.VISIBLE);
                            chip_Wed.setAllCaps(true);
                            chip_Wed.setClickable(false);
                        }
                        if(sp_isAvailableThu == true){
                            chip_Thu.setVisibility(View.VISIBLE);
                            chip_Thu.setAllCaps(true);
                            chip_Thu.setClickable(false);
                        }
                        if(sp_isAvailableFri == true){

                            chip_Fri.setVisibility(View.VISIBLE);
                            chip_Fri.setAllCaps(true);
                            chip_Fri.setClickable(false);
                        }
                        if(sp_isAvailableSat == true){

                            chip_Sat.setVisibility(View.VISIBLE);
                            chip_Sat.setAllCaps(true);
                            chip_Sat.setClickable(false);
                        }
                        if(sp_isAvailableSun == true){

                            chip_Sun.setVisibility(View.VISIBLE);
                            chip_Sun.setAllCaps(true);
                            chip_Sun.setClickable(false);
                        }

                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
                else
                {
                    Toast.makeText(booking_page.this, "Empty", Toast.LENGTH_SHORT).show();
                    System.out.println("Empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booking_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}