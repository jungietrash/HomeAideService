package com.homeaide.post.bookingv3.adaptorsfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homeaide.post.bookingv3.booking.Projects;
import com.homeaide.post.R;
import com.homeaide.post.bookingv3.booking.edit_project_page;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment1Project#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment1Project extends Fragment {

    private List<Projects> arr;
    private AdapterProjectItem adapterItem;
    private String userID;
    private String projectID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment1Project() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment1Project newInstance(String param1, String param2) {
        fragment1Project fragment = new fragment1Project();
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
        View view = inflater.inflate(R.layout.fragment1, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference projDatabase = FirebaseDatabase.getInstance().getReference("Projects");
        userID = user.getUid();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arr = new ArrayList<>();
        adapterItem = new AdapterProjectItem(arr);
        recyclerView.setAdapter(adapterItem);

        Query query = FirebaseDatabase.getInstance().getReference("Projects")
                .orderByChild("userID")
                .equalTo(userID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Projects project = dataSnapshot.getValue(Projects.class);
                    arr.add(project);
                }

                adapterItem.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapterItem.setOnItemClickListener(new AdapterProjectItem.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                arr.get(position);

                Query query = FirebaseDatabase.getInstance().getReference("Projects")
                        .orderByChild("projName")
                        .equalTo(arr.get(position).getProjName());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                            projectID = dataSnapshot.getKey().toString();
                            Intent intentProject = new Intent(getContext(), edit_project_page.class);
                            intentProject.putExtra("Project ID", projectID);
                            startActivity(intentProject);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapterItem.notifyItemChanged(position);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}