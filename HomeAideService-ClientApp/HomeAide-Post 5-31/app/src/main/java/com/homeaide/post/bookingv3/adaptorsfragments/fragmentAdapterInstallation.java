package com.homeaide.post.bookingv3.adaptorsfragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class fragmentAdapterInstallation extends FragmentStateAdapter {
    public fragmentAdapterInstallation(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
            switch(position)
            {
                case 1:
                    return new ServiceElectricalFragment();
                case 2:
                    return new ServiceCleaningFragment();
                case 3:
                    return new ServicePlumbingFragment();
                default:
                    return new ServiceHandyManFragment();
            }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
