package com.example.testing01.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.testing01.User_Cleaning;
import com.example.testing01.User_Electrical;
import com.example.testing01.User_Handyman;
import com.example.testing01.User_Plumbing;
import com.example.testing01.second_page;
import com.example.testing01.R;
import com.example.testing01.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        View.OnClickListener handy= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_Handyman.class);
                startActivity(intent);
            }

        };
        View.OnClickListener electric= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_Electrical.class);
                startActivity(intent);
            }

        };
        View.OnClickListener plumbing= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_Plumbing.class);
                startActivity(intent);
            }

        };
        View.OnClickListener cleaning= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_Cleaning.class);
                startActivity(intent);
            }

        };

        ImageView btn =(ImageView) view.findViewById(R.id.handyman);
        btn.setOnClickListener(handy);
        ImageView btn1 =(ImageView) view.findViewById(R.id.electrical);
        btn1.setOnClickListener(electric);
        ImageView btn2 =(ImageView) view.findViewById(R.id.plumbing);
        btn2.setOnClickListener(plumbing);
        ImageView btn3 =(ImageView) view.findViewById(R.id.cleaningButton);
        btn3.setOnClickListener(cleaning);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}