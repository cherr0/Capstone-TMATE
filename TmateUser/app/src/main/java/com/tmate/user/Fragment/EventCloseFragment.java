package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tmate.user.R;
import com.tmate.user.adapter.EventPagerAdapter;

public class EventCloseFragment extends Fragment {
    private FragmentPagerAdapter epa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event_close,container,false);


        ViewPager vp = v.findViewById(R.id.ViewPager2);
        epa = new EventPagerAdapter(getFragmentManager());

        TabLayout tl = v.findViewById(R.id.tab_layout);
        vp.setAdapter(new EventPagerAdapter (getChildFragmentManager()));
        tl.setupWithViewPager(vp);

        return v;
    }


}
