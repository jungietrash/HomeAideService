package com.example.testing01.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testing01.R;
import com.example.testing01.Setting_Logout;
import com.example.testing01.Setting_Terms_and_Condition;
import com.example.testing01.databinding.FragmentSettingBinding;
import com.example.testing01.second_page;


public class SettingFragment extends Fragment {

    TextView profileTxt,bankTxt, locationTxt, aboutusTxt, termsTxt,logoutTxt;
    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_setting,container,false);

        profileTxt = view.findViewById(R.id.profile);
        profileTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), second_page.class);
                    startActivity(intent);
                }
            });
        bankTxt = view.findViewById(R.id.bank);
        bankTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), second_page.class);
                startActivity(intent);
            }
        });
        locationTxt = view.findViewById(R.id.location);
        locationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), second_page.class);
                startActivity(intent);
            }
        });
        aboutusTxt = view.findViewById(R.id.aboutus);
        aboutusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), second_page.class);
                startActivity(intent);
            }
        });
        termsTxt = view.findViewById(R.id.termsandconditionss);
        termsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_Terms_and_Condition.class);
                startActivity(intent);
            }
        });
        logoutTxt = view.findViewById(R.id.signout);
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_Logout.class);
                startActivity(intent);
            }
        });

            return view;
        }




        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

}