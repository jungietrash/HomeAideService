package com.example.testing01.ui.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testing01.R;
import com.example.testing01.databinding.FragmentBookingBinding;

public class BookingFragment extends Fragment {

    View view;
    private FragmentBookingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BookingViewModel bookingViewModel =
                new ViewModelProvider(this).get(BookingViewModel.class);

        binding = FragmentBookingBinding.inflate(inflater, container, false);
        view = inflater.inflate(R.layout.fragment_booking,container,false);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}