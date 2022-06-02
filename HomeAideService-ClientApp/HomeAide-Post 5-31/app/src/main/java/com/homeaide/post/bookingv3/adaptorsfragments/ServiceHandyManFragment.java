package com.homeaide.post.bookingv3.adaptorsfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homeaide.post.Maps.Latitude;
import com.homeaide.post.bookingv3.booking.Projects;
import com.homeaide.post.R;
import com.homeaide.post.bookingv3.booking.booking_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceHandyManFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceHandyManFragment extends Fragment {

    private List<Projects> arrProjects;
    private AdapterInstallerItem adapterInstallerItem;
    private String userID, projectID, projCategory;
    private FirebaseUser user;
    private DatabaseReference projDatabase, marketDatabase;
    private TextView sortRating, sortPrice, sortLocation;

    private TextView tv_category;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ServiceHandyManFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceHandyManFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceHandyManFragment newInstance(String param1, String param2) {
        ServiceHandyManFragment fragment = new ServiceHandyManFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.service_fragment_handyman, container, false);

        sortRating =  view.findViewById(R.id.iv_sort);
        sortPrice =  view.findViewById(R.id.iv_prices);
        tv_category = (TextView) view.findViewById(R.id.tv_category);
        sortLocation =  view.findViewById(R.id.iv_sortlocation);
        projCategory = "Handyman";

        user = FirebaseAuth.getInstance().getCurrentUser();
        projDatabase = FirebaseDatabase.getInstance().getReference("Projects");
        userID = user.getUid();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewInstallers);
        recyclerView.setHasFixedSize(true);

        arrProjects = new ArrayList<>();
        adapterInstallerItem = new AdapterInstallerItem(arrProjects);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterInstallerItem);

        onClickToGetKeyProj();
        getServices();


        sortRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                arrProjects.clear();
                onClickToGetKeyProj();
                getServicesRatingSorted();
            }
        });
        sortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                arrProjects.clear();
                onClickToGetKeyProj();
                getServicesPriceSorted();
            }
        });
        sortLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                arrProjects.clear();
                onClickToGetKeyProj();
                getServicesNearestSorted();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {

        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;

    }


    private void getServicesRatingSorted() {
        Query query = projDatabase.orderByChild("category").equalTo(projCategory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Projects projData = snapshot.getValue(Projects.class);
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        arrProjects.add(projects);
                    }
                    sortRating();
                    adapterInstallerItem.notifyDataSetChanged();
                }
                tv_category.setText(projCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getServicesPriceSorted() {
        Query query = projDatabase.orderByChild("category").equalTo(projCategory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Projects projData = snapshot.getValue(Projects.class);
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        arrProjects.add(projects);
                    }
                    sortPrice();
                    adapterInstallerItem.notifyDataSetChanged();
                }
                tv_category.setText(projCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getServices() {
        Query query = projDatabase.orderByChild("category").equalTo(projCategory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Projects projData = snapshot.getValue(Projects.class);
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        arrProjects.add(projects);
                    }

                    adapterInstallerItem.notifyDataSetChanged();
                }
                tv_category.setText(projCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getServicesNearestSorted() {
        Query query = projDatabase.orderByChild("category").equalTo(projCategory);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Projects projData = snapshot.getValue(Projects.class);
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Projects projects = dataSnapshot.getValue(Projects.class);
                        arrProjects.add(projects);
                    }
                    sortLocation();
                    adapterInstallerItem.notifyDataSetChanged();
                }
                tv_category.setText(projCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void onClickToGetKeyProj() {

        adapterInstallerItem.setOnItemClickListener(new AdapterInstallerItem.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                arrProjects.get(position);

                Query query = projDatabase
                        .orderByChild("projName")
                        .equalTo(arrProjects.get(position).getProjName());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                            projectID = dataSnapshot.getKey().toString();
                            Intent intentProject = new Intent(getContext(), booking_page.class);
                            intentProject.putExtra("Project ID", projectID);
                            startActivity(intentProject);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapterInstallerItem.notifyItemChanged(position);
            }
        });

    }



    private void sortRating() {
        Collections.sort(arrProjects, new Comparator<Projects>() {
            @Override
            public int compare(Projects o1, Projects o2) {
                            /*
                            Double sides1 = haversine(Double.parseDouble(o1.getLatitude()), Double.parseDouble(o1.getLongitude()), 10.306915063412701, 123.90566221441806);
                            Double sides2 = haversine(Double.parseDouble(o2.getLatitude()), Double.parseDouble(o2.getLongitude()), 10.306915063412701, 123.90566221441806);
                             */
                Double sides1 = Double.parseDouble(o1.getRatings());
                Double sides2 = Double.parseDouble(o2.getRatings());
                return new Float(sides2).compareTo(new Float(sides1));
            }
        });
        Toast.makeText(getContext(), "Sort Highest to Lowest Rating", Toast.LENGTH_SHORT).show();
    }


    private void sortPrice() {
        Collections.sort(arrProjects, new Comparator<Projects>() {
            @Override
            public int compare(Projects o1, Projects o2) {
                /*
               Double sides1 = haversine(Double.parseDouble(o1.getLatitude()), Double.parseDouble(o1.getLongitude()), 10.306915063412701, 123.90566221441806);
               Double sides2 = haversine(Double.parseDouble(o2.getLatitude()), Double.parseDouble(o2.getLongitude()), 10.306915063412701, 123.90566221441806);
                 */
                Double sides1 = Double.parseDouble(o1.getPrice());
                Double sides2 = Double.parseDouble(o2.getPrice());

                return new Float(sides1).compareTo(new Float(sides2));
            }
        });
        Toast.makeText(getContext(), "Sort Lowest to Highest Price", Toast.LENGTH_SHORT).show();
    }


    private void sortLocation() {
        Collections.sort(arrProjects, new Comparator<Projects>() {
            @Override
            public int compare(Projects o1, Projects o2) {

                Latitude lt = new Latitude();
                //  Double userLat = 10.318281696356713;
                //  Double userLong = 123.8954437203891;

                Double userLat = lt.getuserLatitude();
                Double userLong = lt.getuserLongitude();


                Double sides1 = haversine(Double.parseDouble(o1.getLatitude()), Double.parseDouble(o1.getLongitude()), userLat, userLong);
                Double sides2 = haversine(Double.parseDouble(o2.getLatitude()), Double.parseDouble(o2.getLongitude()), userLat, userLong);

                return new Float(sides1).compareTo(new Float(sides2));
            }
        });
        Toast.makeText(getContext(), "Recommending Nearest to Farthest", Toast.LENGTH_SHORT).show();
    }
}
