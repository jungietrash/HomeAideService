package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.homeaide.post.RatingActivity;
import com.homeaide.post.bookingv3.adaptorsfragments.AdapterInstallerItem;
import com.homeaide.post.R;
import com.homeaide.post.chat.ChatMainActivity;

public class search_page extends AppCompatActivity {

    private ImageView iv_messageBtn, iv_notificationBtn, iv_homeBtn, iv_accountBtn,
            iv_moreBtn;
    private TextView tv_back, tv_headerTitle;
    private SearchView sv_search;
    private ImageView backBtn;
    private RecyclerView recyclerView_searches;
    private ProgressBar progressBar;
    private String listOfCategory = "";

    private ArrayAdapter<CharSequence> adapterCategoryItems;
    private AdapterInstallerItem adapterInstallerItem;
    private DatabaseReference projDatabase;
    private ArrayList<Projects> arrProj;
    private ArrayList<String> arrCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        projDatabase = FirebaseDatabase.getInstance().getReference("Projects");

        arrProj = new ArrayList<>();
        arrCategory = new ArrayList<>();
        tv_back = findViewById(R.id.tv_back);
        sv_search = findViewById(R.id.sv_search);
        recyclerView_searches = findViewById(R.id.recyclerView_searches);
        progressBar = findViewById(R.id.progressBar);
        backBtn = findViewById(R.id.backBtn);

        generateRecyclerLayout();
        generateListOfCategory();
        generateDataValue();
        clickListeners();

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
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
                        startActivity(new Intent(getApplicationContext(), my_booking_page.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_setting:
                        startActivity(new Intent(getApplicationContext(), more_page.class));
                        overridePendingTransition(0,0);

                    case R.id.navigation_home:

                        return true;

                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage.class);
                startActivity(intent);
            }
        });
    }

    private void generateRecyclerLayout() {
        recyclerView_searches.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_searches.setLayoutManager(linearLayoutManager);

        adapterInstallerItem = new AdapterInstallerItem(arrProj);
        recyclerView_searches.setAdapter(adapterInstallerItem);

    }

    private void generateListOfCategory() {
        projDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        String category = projects.getCategory();
                        listOfCategory = category +","+ listOfCategory;
                        arrCategory.add(category);
                    }

                    progressBar.setVisibility(View.GONE);
                    adapterInstallerItem.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateRecyclerLayoutByCategory( String category) {
        Query query = projDatabase
                .orderByChild("category")
                .equalTo(category);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        arrProj.add(projects);

                    }
                }

                AdapterInstallerItem adapter = new AdapterInstallerItem(arrProj);
                recyclerView_searches.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataValue() {
        if(projDatabase != null)
        {
            generateAllProjects();

        }
        if(sv_search != null)
        {
            sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void generateAllProjects() {

        projDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        arrProj.add(projects);
                    }

                    progressBar.setVisibility(View.GONE);
                    adapterInstallerItem.notifyDataSetChanged();
                }
                AdapterInstallerItem adapter = new AdapterInstallerItem(arrProj);
                recyclerView_searches.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void clickListeners() {

        adapterInstallerItem.setOnItemClickListener(new AdapterInstallerItem.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void search(String s) {
        ArrayList<Projects> arr = new ArrayList<>();
        for(Projects object : arrProj)
        {
            if(object.getProjName().toLowerCase().contains(s.toLowerCase()))
            {
                arr.add(object);
            }
            AdapterInstallerItem adapter = new AdapterInstallerItem(arr);
            recyclerView_searches.setAdapter(adapter);
        }
    }
}