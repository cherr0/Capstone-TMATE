package com.tmate.user.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EventPagerAdapter extends FragmentPagerAdapter {
    public EventPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return EventProFragment.newInstance();
            case 1 :
                return EventStopFragment.newInstance();
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "진행중인 이벤트";
            case 1 :
                return "중단한 이벤트";
            default: return null;
        }
    }
}
