package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.homeaide.post.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.homeaide.post.RatingActivity;
import com.homeaide.post.bookingv3.adaptorsfragments.AdapterMyBookings;
import com.homeaide.post.chat.ChatMainActivity;

public class my_booking_page extends AppCompatActivity {

    private List<Booking> arr;
    private AdapterMyBookings adapterMyBookings;
    private DatabaseReference cartDatabase;
    private String userID, bookingID;

    private ImageView iv_messageBtn, iv_notificationBtn, iv_homeBtn, iv_accountBtn,
            iv_moreBtn, item3;
    private RecyclerView recyclerView_myBookings;
    private TextView tv_back;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_booking_page);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        cartDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        userID = user.getUid();

        setRef();
        generateRecyclerView();
        clickListeners();

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_booking);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(), ChatMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_booking:



                    case R.id.navigation_setting:
                        startActivity(new Intent(getApplicationContext(), more_page.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), homepage.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void clickListeners() {

        adapterMyBookings.setOnItemClickListener(new AdapterMyBookings.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                final ProgressDialog progressDialog = new ProgressDialog(my_booking_page.this);
                progressDialog.setTitle("Loading...");
                progressDialog.show();

                arr.get(position);

                Query query = FirebaseDatabase.getInstance().getReference("Bookings")
                        .orderByChild("projName")
                        .equalTo(arr.get(position).getProjName());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            bookingID = dataSnapshot.getKey().toString();
                            Intent intent = new Intent(my_booking_page.this, client_booking_details.class);
                            intent.putExtra("Booking ID", bookingID);
                            startActivity(intent);

                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(my_booking_page.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

                adapterMyBookings.notifyItemChanged(position);


            }
        });
    }

    private void generateRecyclerView() {

        recyclerView_myBookings.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_myBookings.setLayoutManager(linearLayoutManager);

        arr = new ArrayList<>();
        adapterMyBookings = new AdapterMyBookings(arr);
        recyclerView_myBookings.setAdapter(adapterMyBookings);

        Query query = cartDatabase
                .orderByChild("custID")
                .equalTo(userID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Booking booking = dataSnapshot.getValue(Booking.class);
                    arr.add(booking);

                }

                progressBar.setVisibility(View.GONE);
                adapterMyBookings.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void setRef() {


        tv_back = findViewById(R.id.tv_back);

        recyclerView_myBookings = findViewById(R.id.recyclerView_myBookings);

        progressBar = findViewById(R.id.progressBar);
    }
}