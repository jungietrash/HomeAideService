package com.homeaide.post.Booking.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.homeaide.post.HomeActivityForAdmin;
import com.homeaide.post.Booking.adaptorsfragments.AdapterInstallerItem;
import com.homeaide.post.R;

public class Admin_Search_Page extends AppCompatActivity {

    private SearchView searchView;
    private ImageView backBTN;
    private RecyclerView recycleViewSearch;
    private String listOfCategory = "";

    private ArrayAdapter<CharSequence> adapterCategoryItems;
    private AdapterInstallerItem adapterInstallerItem;
    private DatabaseReference serviceDatabase;
    private ArrayList<Services> arrayServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_search_page);

        serviceDatabase = FirebaseDatabase.getInstance().getReference("Services");
        arrayServices = new ArrayList<>();
        searchView = findViewById(R.id.searchView);
        recycleViewSearch = findViewById(R.id.recycleViewSearch);
        backBTN = findViewById(R.id.backBTN);

        recycleViewSearch.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleViewSearch.setLayoutManager(linearLayoutManager);

        adapterInstallerItem = new AdapterInstallerItem(arrayServices);
        recycleViewSearch.setAdapter(adapterInstallerItem);

        displayData();

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Search_Page.this, HomeActivityForAdmin.class);
                startActivity(intent);
            }
        });
    }

    private void displayData() {
        if(serviceDatabase != null)
        {
            AllService();

        }
        if(searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return false;
                }
            });
        }
    }

    private void AllService() {

        serviceDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Services projects = dataSnapshot.getValue(Services.class);
                        arrayServices.add(projects);
                    }

                    adapterInstallerItem.notifyDataSetChanged();
                }
                AdapterInstallerItem adapter = new AdapterInstallerItem(arrayServices);
                recycleViewSearch.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Search_Page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void search(String s) {
        ArrayList<Services> arr = new ArrayList<>();
        for(Services object : arrayServices)
        {
            if(object.getProjName().toLowerCase().contains(s.toLowerCase()))
            {
                arr.add(object);
            }
            AdapterInstallerItem adapter = new AdapterInstallerItem(arr);
            recycleViewSearch.setAdapter(adapter);
        }
    }
}