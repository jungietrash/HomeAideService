package com.homeaide.post.bookingv3.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import com.homeaide.post.RatingActivity;
import com.homeaide.post.bookingv3.adaptorsfragments.fragmentAdapterInstallation;
import com.homeaide.post.R;
import com.homeaide.post.chat.ChatMainActivity;

public class Handyman_Service_Page extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager2 vp_viewPager2;
    private fragmentAdapterInstallation adapter;
    private int currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handyman_starting_page);

        currentTab = getIntent().getIntExtra("tabNum", 0);

        setRef();
        generateTabLayout();

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
                        overridePendingTransition(0,120);
                        return true;

                    case R.id.navigation_booking:
                        startActivity(new Intent(getApplicationContext(), my_booking_page.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_setting:
                        startActivity(new Intent(getApplicationContext(), more_page.class));
                        overridePendingTransition(0,0);

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), homepage.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
    }

    private void generateTabLayout() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new fragmentAdapterInstallation(fragmentManager, getLifecycle());
        vp_viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("HandyMan"));
        tabLayout.addTab(tabLayout.newTab().setText("Electrical"));
        tabLayout.addTab(tabLayout.newTab().setText("Cleaning"));
        tabLayout.addTab(tabLayout.newTab().setText("Plumbing"));

        tabLayout.setScrollPosition(currentTab,0f,true);
        vp_viewPager2.setCurrentItem(currentTab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               vp_viewPager2.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp_viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }


    private void setRef() {

        tabLayout = findViewById(R.id.tab_layout);

        vp_viewPager2 = findViewById(R.id.vp_viewPager2);

    }


}